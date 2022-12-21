/*
 * Copyright (c) 2020-2021 Pcap Project
 * SPDX-License-Identifier: MIT OR Apache-2.0
 */

package com.github.waifu.packets.packetcapture.sniff.ardikars;

import com.github.waifu.annotation.AllowPublicAccess;
import com.sun.jna.Callback;
import com.sun.jna.FromNativeContext;
import com.sun.jna.FunctionMapper;
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
import com.sun.jna.NativeLong;
import com.sun.jna.Platform;
import com.sun.jna.Pointer;
import com.sun.jna.PointerType;
import com.sun.jna.Structure;
import com.sun.jna.ptr.PointerByReference;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import pcap.common.logging.Logger;
import pcap.common.logging.LoggerFactory;
import pcap.jdk7.internal.NativeSignature;
import pcap.spi.Address;
import pcap.spi.Interface;
import pcap.spi.Timestamp;
import pcap.spi.annotation.Version;

/**
 * Directly extracted out of ardikars library to make edits possible.
 * https://github.com/ardikars/pcap
 */
public final class NativeMappings {

  /**
   * To be documented.
   */
  static final int RESTRICTED_LEVEL;
  /**
   * To be documented.
   */
  static final int RESTRICTED_LEVEL_DENY = 0;
  /**
   * To be documented.
   */
  static final int RESTRICTED_LEVEL_PERMIT = 1;
  /**
   * To be documented.
   */
  static final int RESTRICTED_LEVEL_WARN = 2;
  /**
   * To be documented.
   */
  static final String RESTRICTED_MESSAGE =
          "Access to restricted method is disabled by default; to enabled access to restricted method, the Pcap property 'pcap.restricted' must be set to a value other then deny.";
  /**
   * To be documented.
   */
  static final String RESTRICTED_PROPERTY_VALUE =
          "The possible values for this property are:\n"
                  + "0) deny: issues a runtime exception on each restricted call. This is the default value;\n"
                  + "1) permit: allows restricted calls;\n"
                  + "2) warn: like permit, but also prints a one-line warning on each restricted call.\n";
  /**
   * To be documented.
   */
  static final int OK = 0;
  /**
   * To be documented.
   */
  static final int TRUE = 1;
  /**
   * To be documented.
   */
  static final int FALSE = 0;
  /**
   * To be documented.
   */
  static final short AF_INET;
  /**
   * To be documented.
   */
  static final short AF_INET6;
  /**
   * To be documented.
   */
  static final DefaultPlatformDependent PLATFORM_DEPENDENT;
  /**
   * To be documented.
   */
  static final boolean IS_WIN_PCAP;
  /**
   * To be documented.
   */
  private static final Logger LOG = LoggerFactory.getLogger(NativeMappings.class);
  /**
   * To be documented.
   */
  private static final Map<String, Object> NATIVE_LOAD_LIBRARY_OPTIONS = new HashMap<String, Object>();

  static {
    com.sun.jna.Native.register(NativeMappings.class, NativeLibrary.getInstance(libName(Platform.isWindows())));

    // for interface mapping
    final Map<String, String> funcMap = new HashMap<String, String>();
    funcMap.put("pcap_dump_open_append", "pcap_dump_open_append");
    funcMap.put("pcap_get_tstamp_precision", "pcap_get_tstamp_precision");
    funcMap.put("pcap_set_tstamp_type", "pcap_set_tstamp_type");
    funcMap.put("pcap_set_rfmon", "pcap_set_rfmon");
    funcMap.put("pcap_open_offline_with_tstamp_precision", "pcap_open_offline_with_tstamp_precision");
    funcMap.put("pcap_set_tstamp_precision", "pcap_set_tstamp_precision");
    funcMap.put("pcap_set_immediate_mode", "pcap_set_immediate_mode");
    funcMap.put("pcap_setmintocopy", "pcap_setmintocopy");
    funcMap.put("pcap_get_selectable_fd", "pcap_get_selectable_fd");
    funcMap.put("pcap_get_required_select_timeout", "pcap_get_required_select_timeout");
    funcMap.put("pcap_getevent", "pcap_getevent");
    funcMap.put("pcap_statustostr", "pcap_statustostr");
    funcMap.put("pcap_inject", "pcap_inject");
    funcMap.put("pcap_dump_ftell", "pcap_dump_ftell");
    funcMap.put("pcap_setdirection", "pcap_setdirection");
    funcMap.put("pcap_create", "pcap_create");
    funcMap.put("pcap_set_snaplen", "pcap_set_snaplen");
    funcMap.put("pcap_set_promisc", "pcap_set_promisc");
    funcMap.put("pcap_set_timeout", "pcap_set_timeout");
    funcMap.put("pcap_set_buffer_size", "pcap_set_buffer_size");
    funcMap.put("pcap_activate", "pcap_activate");

    NATIVE_LOAD_LIBRARY_OPTIONS.put(
          Library.OPTION_FUNCTION_MAPPER,
          new FunctionMapper() {
            @Override
            public String getFunctionName(final NativeLibrary library, final Method method) {
              return funcMap.get(method.getName());
            }
          });
    PLATFORM_DEPENDENT = new DefaultPlatformDependent();
    IS_WIN_PCAP = pcap_lib_version().toLowerCase().contains("winpcap");

    AF_INET = 2;
    AF_INET6 = defaultAfInet6();

    final String characterEncoding = System.getProperty("pcap.character.encoding");
    initLibrary(characterEncoding);
    final String unsafeAccess = System.getProperty("pcap.restricted", "deny");
    if (unsafeAccess.equals("deny")) {
      RESTRICTED_LEVEL = RESTRICTED_LEVEL_DENY;
    } else if (unsafeAccess.equals("permit")) {
      RESTRICTED_LEVEL = RESTRICTED_LEVEL_PERMIT;
    } else if (unsafeAccess.equals("warn")) {
      RESTRICTED_LEVEL = RESTRICTED_LEVEL_WARN;
    } else {
      RESTRICTED_LEVEL = RESTRICTED_LEVEL_DENY;
    }
  }

  private NativeMappings() {
  }

  static void initLibrary(final String characterEncoding) {
    if (characterEncoding != null) {
      /*
       * Initialization options.
       * All bits not listed here are reserved for expansion.
       *
       * On UNIX-like systems, the local character encoding is assumed to be
       * UTF-8, so no character encoding transformations are done.
       *
       * On Windows, the local character encoding is the local ANSI code page.
       */
      final int PCAP_CHAR_ENC_LOCAL = 0x00000000; /* strings are in the local character encoding */
      final int PCAP_CHAR_ENC_UTF_8 = 0x00000001; /* strings are in UTF-8 */
      final int rc;
      final ErrorBuffer errbuf = new ErrorBuffer();
      errbuf.clear();
      errbuf.buf[0] = '\0';
      if (characterEncoding.equals("UTF-8")) {
        rc = PLATFORM_DEPENDENT.pcap_init(PCAP_CHAR_ENC_UTF_8, errbuf);
      } else {
        rc = PLATFORM_DEPENDENT.pcap_init(PCAP_CHAR_ENC_LOCAL, errbuf);
      }
      eprint(rc, errbuf);
    }
  }

  static void eprint(final int rc, final ErrorBuffer errbuf) {
    if (rc != 0) {
      LOG.warn(errbuf.toString());
    }
  }

  static String libName(final boolean isWindows) {
    if (isWindows) {
      return "wpcap";
    } else {
      return "pcap";
    }
  }

  static short defaultAfInet6() {
    short tmpAfInet6 = 0;
    final String afInet6 = System.getProperty("pcap.af.inet6");
    try {
      tmpAfInet6 = (short) Integer.parseInt(afInet6);
    } catch (final NumberFormatException e) {
      switch (Platform.getOSType()) {
        case Platform.MAC:
          tmpAfInet6 = 30;
          break;
        case Platform.KFREEBSD:
          tmpAfInet6 = 28;
          break;
        case Platform.LINUX:
          tmpAfInet6 = 10;
          break;
        default:
          tmpAfInet6 = 23;
      }
    }
    return tmpAfInet6;
  }

  @NativeSignature(
          signature = "const char *pcap_lib_version(void)",
          since = @Version(major = 0, minor = 8))
  static native String pcap_lib_version();

  @NativeSignature(
          signature = "char *pcap_geterr(pcap_t *p)",
          since = @Version(major = 0, minor = 4))
  static native Pointer pcap_geterr(Pointer p);

  @NativeSignature(
          signature = "void pcap_freealldevs(pcap_if_t *alldevs)",
          since = @Version(major = 0, minor = 7))
  static native int pcap_findalldevs(PointerByReference alldevsp, ErrorBuffer errbuf);

  @NativeSignature(
          signature = "void pcap_freealldevs(pcap_if_t *alldevs)",
          since = @Version(major = 0, minor = 7))
  static native void pcap_freealldevs(Pointer p);

  @NativeSignature(
          signature = "pcap_t *pcap_open_offline(const char *fname, char *errbuf)",
          since = @Version(major = 0, minor = 4))
  static native Pointer pcap_open_live(
          String device, int snaplen, int promisc, int toMs, ErrorBuffer errbuf);

  @NativeSignature(
          signature = "pcap_t *pcap_open_offline(const char *fname, char *errbuf)",
          since = @Version(major = 0, minor = 4))
  static native Pointer pcap_open_offline(String fname, ErrorBuffer errbuf);

  @NativeSignature(
          signature = "int pcap_loop(pcap_t *p, int cnt, PcapHandler callback, u_char *user)",
          since = @Version(major = 0, minor = 4))
  static native int pcap_loop(Pointer p, int cnt, PcapHandler callback, Pointer user);

  @NativeSignature(
          signature = "int pcap_dispatch(pcap_t *p, int cnt, PcapHandler callback, u_char *user)",
          since = @Version(major = 0, minor = 4))
  static native int pcap_dispatch(Pointer p, int cnt, PcapHandler callback, Pointer user);

  @NativeSignature(
          signature = "int pcap_sendpacket(pcap_t *p, const u_char *buf, int size)",
          since = @Version(major = 0, minor = 8))
  static native int pcap_sendpacket(Pointer p, Pointer buf, int size);

  @NativeSignature(
          signature =
                  "int pcap_compile(pcap_t *p, struct BpfProgram *fp, const char *str, int optimize, bpf_u_int32 netmask)",
          since = @Version(major = 0, minor = 4))
  static native int pcap_compile(Pointer p, BpfProgram fp, String str, int optimize, int netmask);

  @NativeSignature(
          signature = "int pcap_setfilter(pcap_t *p, struct BpfProgram *fp)",
          since = @Version(major = 0, minor = 4))
  static native int pcap_setfilter(Pointer p, BpfProgram fp);

  @NativeSignature(
          signature = "void pcap_freecode(struct BpfProgram *)",
          since = @Version(major = 0, minor = 6))
  static native void pcap_freecode(BpfProgram fp);

  @NativeSignature(signature = "void pcap_close(pcap_t *p)", since = @Version(major = 0, minor = 4))
  static native void pcap_close(Pointer p);

  @NativeSignature(signature = "pcap_breakloop", since = @Version(major = 0, minor = 8))
  static native void pcap_breakloop(Pointer p);

  @NativeSignature(
          signature =
                  "int pcap_next_ex(pcap_t *p, struct PcapPkthdr **pkt_header, const u_char **pkt_data)",
          since = @Version(major = 0, minor = 8))
  static native int pcap_next_ex(Pointer p, PointerByReference h, PointerByReference data);

  @NativeSignature(
          signature = "pcap_dumper_t *pcap_dump_open(pcap_t *p, const char *fname)",
          since = @Version(major = 0, minor = 4))
  static native Pointer pcap_dump_open(Pointer p, String fname);

  @NativeSignature(
          signature = "void pcap_dump(u_char *user, struct PcapPkthdr *h, u_char *sp)",
          since = @Version(major = 0, minor = 4))
  static native void pcap_dump(Pointer user, Pointer header, Pointer packet);

  @NativeSignature(
          signature = "int pcap_dump_flush(pcap_dumper_t *p)",
          since = @Version(major = 0, minor = 8))
  static native int pcap_dump_flush(Pointer p);

  @NativeSignature(
          signature = "void pcap_dump_close(pcap_dumper_t *p)",
          since = @Version(major = 0, minor = 4))
  static native void pcap_dump_close(Pointer p);

  @NativeSignature(
          signature = "int pcap_stats(pcap_t *p, struct pcap_stat *ps)",
          since = @Version(major = 0, minor = 4))
  static native int pcap_stats(Pointer p, Pointer ps);

  @NativeSignature(
          signature = "int pcap_setnonblock(pcap_t *p, int nonblock, char *errbuf)",
          since = @Version(major = 0, minor = 7))
  static native int pcap_setnonblock(Pointer p, int nonblock, ErrorBuffer errbuf);

  @NativeSignature(
          signature = "int pcap_getnonblock(pcap_t *p, char *errbuf)",
          since = @Version(major = 0, minor = 7))
  static native int pcap_getnonblock(Pointer p, ErrorBuffer errbuf);

  @NativeSignature(
          signature = "int pcap_minor_version(pcap_t *p)",
          since = @Version(major = 0, minor = 4))
  static native int pcap_minor_version(Pointer p);

  @NativeSignature(
          signature = "int pcap_snapshot(pcap_t *p)",
          since = @Version(major = 0, minor = 4))
  static native int pcap_snapshot(Pointer p);

  @NativeSignature(
          signature = "int pcap_major_version(pcap_t *p)",
          since = @Version(major = 0, minor = 4))
  static native int pcap_major_version(Pointer p);

  @NativeSignature(
          signature = "int pcap_is_swapped(pcap_t *p)",
          since = @Version(major = 0, minor = 4))
  static native int pcap_is_swapped(Pointer p);

  @NativeSignature(
          signature = "int pcap_datalink(pcap_t *p)",
          since = @Version(major = 0, minor = 4))
  static native int pcap_datalink(Pointer p);

  @NativeSignature(
          signature = "u_int bpf_filter(const struct BpfInsn *, const u_char *, u_int, u_int)",
          since = @Version(major = 0, minor = 4))
  static native int bpf_filter(
          BpfInsn insn, Pointer packet, int oriPktLen, int pktLen);

  static InetAddress inetAddress(final SockAddr sockaddr) {
    if (sockaddr == null) {
      return null;
    }
    InetAddress address;
    try {
      if (sockaddr.getSaFamily() == AF_INET) {
        address = InetAddress.getByAddress(Arrays.copyOfRange(sockaddr.saData, 2, 6));
      } else if (sockaddr.saFamily == AF_INET6) {
        address = InetAddress.getByAddress(Arrays.copyOfRange(sockaddr.saData, 2, 18));
      } else {
        address = null;
      }
    } catch (final Exception e) {
      address = null;
    }
    return address;
  }

  interface PcapHandler extends Callback {

    void got_packet(Pointer args, Pointer header, Pointer packet);
  }

  interface PlatformDependent extends Library {

    @NativeSignature(
            signature = "int pcap_inject(pcap_t *p, const void *buf, size_t size)",
            since = @Version(major = 0, minor = 9))
    int pcap_inject(Pointer p, Pointer buf, int size);

    @NativeSignature(
            signature = "long pcap_dump_ftell(pcap_dumper_t *p)",
            since = @Version(major = 0, minor = 9))
    NativeLong pcap_dump_ftell(Pointer dumper);

    @NativeSignature(
            signature = "int pcap_setdirection(pcap_t *p, pcap_direction_t d)",
            since = @Version(major = 0, minor = 9))
    int pcap_setdirection(Pointer p, int pcapDirection);

    @NativeSignature(
            signature = "pcap_t *pcap_create(const char *source, char *errbuf)",
            since = @Version(major = 1, minor = 0))
    Pointer pcap_create(String device, ErrorBuffer errbuf);

    @NativeSignature(
            signature = "int pcap_set_snaplen(pcap_t *p, int snaplen)",
            since = @Version(major = 1, minor = 0))
    int pcap_set_snaplen(Pointer p, int snaplen);

    @NativeSignature(
            signature = "int pcap_set_promisc(pcap_t *p, int promisc)",
            since = @Version(major = 1, minor = 0))
    int pcap_set_promisc(Pointer p, int promisc);

    @NativeSignature(
            signature = "int pcap_set_timeout(pcap_t *p, int to_ms)",
            since = @Version(major = 1, minor = 0))
    int pcap_set_timeout(Pointer p, int timeout);

    @NativeSignature(
            signature = "int pcap_set_buffer_size(pcap_t *p, int buffer_size)",
            since = @Version(major = 1, minor = 0))
    int pcap_set_buffer_size(Pointer p, int bufferSize);

    @NativeSignature(
            signature = "int pcap_activate(pcap_t *p)",
            since = @Version(major = 1, minor = 0))
    int pcap_activate(Pointer p);

    @NativeSignature(
            signature = "int pcap_can_set_rfmon(pcap_t *p)",
            since = @Version(major = 1, minor = 0))
    int pcap_can_set_rfmon(Pointer p);

    @NativeSignature(
            signature = "const char *pcap_statustostr(int error)",
            since = @Version(major = 1, minor = 0))
    String pcap_statustostr(int error);

    @NativeSignature(
            signature = "pcap_dumper_t *pcap_dump_open_append(pcap_t *p, const char *fname)",
            since = @Version(major = 1, minor = 7))
    Pointer pcap_dump_open_append(Pointer p, String fname);

    @NativeSignature(
            signature = "int pcap_set_tstamp_type(pcap_t *p, int tstamp_type)",
            since = @Version(major = 1, minor = 2))
    int pcap_set_tstamp_type(Pointer p, int tstampType);

    @NativeSignature(
            signature = "int pcap_get_tstamp_precision(pcap_t *p)",
            since = @Version(major = 1, minor = 5))
    int pcap_get_tstamp_precision(Pointer p);

    @NativeSignature(
            signature = "int pcap_set_rfmon(pcap_t *p, int rfmon)",
            since = @Version(major = 1, minor = 0))
    int pcap_set_rfmon(Pointer p, int rfmon);

    @NativeSignature(
            signature =
                    "pcap_t *pcap_open_offline_with_tstamp_precision(const char *fname, u_int precision, char *errbuf)",
            since = @Version(major = 1, minor = 5))
    Pointer pcap_open_offline_with_tstamp_precision(
            String fname, int precision, ErrorBuffer errbuf);

    @NativeSignature(
            signature = "int pcap_set_tstamp_precision(pcap_t *p, int tstampPrecision)",
            since = @Version(major = 1, minor = 5))
    int pcap_set_tstamp_precision(Pointer p, int tstampPrecision);

    @NativeSignature(
            signature = "int pcap_set_immediate_mode(pcap_t *p, int immediateMode)",
            since = @Version(major = 1, minor = 5))
    int pcap_set_immediate_mode(Pointer p, int immediateMode);

    @NativeSignature(
            signature = "int pcap_get_selectable_fd(pcap_t *p)",
            since = @Version(major = 0, minor = 8),
            description = "Only available on Unix system.",
            portable = false)
    int pcap_get_selectable_fd(Pointer p);

    @NativeSignature(
            signature = "int pcap_get_required_select_timeout(pcap_t *p)",
            since = @Version(major = 1, minor = 9),
            description = "Only available on Unix system.",
            portable = false)
    Pointer pcap_get_required_select_timeout(Pointer p);

    @NativeSignature(
            signature = "Handle pcap_getevent(pcap_t *p)",
            since = @Version(major = 0, minor = 4),
            description = "Only available on Windows system.",
            portable = false)
    Handle pcap_getevent(Pointer p);

    @NativeSignature(
            signature = "int pcap_setmintocopy(pcap_t *p, int size)",
            since = @Version(major = 0, minor = 4),
            description = "Only available on Windows system.",
            portable = false)
    int pcap_setmintocopy(Pointer p, int size);

    @NativeSignature(
            signature = "int pcap_init(unsigned int opts, char *errbuf)",
            since = @Version(major = 1, minor = 10),
            description = "Used to initialize the Packet Capture library")
    int pcap_init(int opts, ErrorBuffer errbuf);
  }

  static final class DefaultPlatformDependent implements PlatformDependent {

    /**
     * To be documented.
     */
    private static final NativeLong ZERO = new NativeLong(0);

    /**
     * To be documented.
     */
    private static final PlatformDependent NATIVE =
            com.sun.jna.Native.load(
                    libName(Platform.isWindows()), PlatformDependent.class, NATIVE_LOAD_LIBRARY_OPTIONS);

    /**
     * To be documented.
     */
    private final AtomicBoolean injectIsSupported = new AtomicBoolean(true);
    /**
     * To be documented.
     */
    private final AtomicBoolean dumpFtellIsSupported = new AtomicBoolean(true);

    /**
     * To be documented.
     *
     * @param p To be documented.
     * @param buf To be documented.
     * @param size To be documented.
     * @return To be documented.
     */
    @Override
    public int pcap_inject(final Pointer p, final Pointer buf, final int size) {
      if (injectIsSupported.get()) {
        try {
          return NATIVE.pcap_inject(p, buf, size);
        } catch (final NullPointerException | UnsatisfiedLinkError e) {
          LOG.warn("pcap_inject: Function doesn't exist, use pcap_sendpacket.");
          injectIsSupported.compareAndSet(true, false);
        }
      }
      final int rc = pcap_sendpacket(p, buf, size);
      if (rc < 0) {
        return rc;
      } else {
        return size;
      }
    }

    /**
     * To be documented.
     *
     * @param dumper To be documented.
     * @return To be documented.
     */
    @Override
    public NativeLong pcap_dump_ftell(final Pointer dumper) {
      if (dumpFtellIsSupported.get()) {
        try {
          return NATIVE.pcap_dump_ftell(dumper);
        } catch (final NullPointerException | UnsatisfiedLinkError e) {
          LOG.warn("pcap_dump_ftell: Function doesn't exist.");
          dumpFtellIsSupported.compareAndSet(true, false);
        }
      }
      return ZERO;
    }

    /**
     * To be documented.
     *
     * @param p To be documented.
     * @param pcapDirection To be documented.
     * @return To be documented.
     */
    @Override
    public int pcap_setdirection(final Pointer p, final int pcapDirection) {
      try {
        return NATIVE.pcap_setdirection(p, pcapDirection);
      } catch (final NullPointerException | UnsatisfiedLinkError e) {
        LOG.warn("pcap_setdirection: Function doesn't exist.");
        return 0;
      }
    }

    /**
     * To be documented.
     *
     * @param device To be documented.
     * @param errbuf To be documented.
     * @return To be documented.
     */
    @Override
    public Pointer pcap_create(final String device, final ErrorBuffer errbuf) {
      try {
        return NATIVE.pcap_create(device, errbuf);
      } catch (final NullPointerException | UnsatisfiedLinkError e) {
        LOG.warn("pcap_create: Function doesn't exist.");
        return null;
      }
    }

    /**
     * To be documented.
     *
     * @param p To be documented.
     * @param snaplen To be documented.
     * @return To be documented.
     */
    @Override
    public int pcap_set_snaplen(final Pointer p, final int snaplen) {
      try {
        return NATIVE.pcap_set_snaplen(p, snaplen);
      } catch (final NullPointerException | UnsatisfiedLinkError e) {
        LOG.warn("pcap_set_snaplen: Function doesn't exist.");
        return 0;
      }
    }

    /**
     * To be documented.
     *
     * @param p To be documented.
     * @param promisc To be documented.
     * @return To be documented.
     */
    @Override
    public int pcap_set_promisc(final Pointer p, final int promisc) {
      try {
        return NATIVE.pcap_set_promisc(p, promisc);
      } catch (final NullPointerException | UnsatisfiedLinkError e) {
        LOG.warn("pcap_set_promisc: Function doesn't exist.");
        return 0;
      }
    }

    /**
     * To be documented.
     *
     * @param p To be documented.
     * @param timeout To be documented.
     * @return To be documented.
     */
    @Override
    public int pcap_set_timeout(final Pointer p, final int timeout) {
      try {
        return NATIVE.pcap_set_timeout(p, timeout);
      } catch (final NullPointerException | UnsatisfiedLinkError e) {
        LOG.warn("pcap_set_timeout: Function doesn't exist.");
        return 0;
      }
    }

    /**
     * To be documented.
     *
     * @param p To be documented.
     * @param bufferSize To be documented.
     * @return To be documented.
     */
    @Override
    public int pcap_set_buffer_size(final Pointer p, final int bufferSize) {
      try {
        return NATIVE.pcap_set_buffer_size(p, bufferSize);
      } catch (final NullPointerException | UnsatisfiedLinkError e) {
        LOG.warn("pcap_set_buffer_size: Function doesn't exist.");
        return 0;
      }
    }

    /**
     * To be documented.
     *
     * @param p To be documented.
     * @return To be documented.
     */
    @Override
    public int pcap_activate(final Pointer p) {
      try {
        return NATIVE.pcap_activate(p);
      } catch (final NullPointerException | UnsatisfiedLinkError e) {
        LOG.warn("pcap_activate: Function doesn't exist.");
        return -1;
      }
    }

    /**
     * To be documented.
     *
     * @param p To be documented.
     * @return To be documented.
     */
    @Override
    public int pcap_can_set_rfmon(final Pointer p) {
      try {
        return NATIVE.pcap_can_set_rfmon(p);
      } catch (final NullPointerException | UnsatisfiedLinkError e) {
        LOG.warn("pcap_can_set_frmon: Function doesn't exist.");
        return 0;
      }
    }

    /**
     * To be documented.
     *
     * @param error To be documented.
     * @return To be documented.
     */
    @Override
    public String pcap_statustostr(final int error) {
      try {
        return NATIVE.pcap_statustostr(error);
      } catch (final NullPointerException | UnsatisfiedLinkError e) {
        return "pcap_statustostr: Function doesn't exist.";
      }
    }

    /**
     * To be documented.
     *
     * @param p To be documented.
     * @param fname To be documented.
     * @return To be documented.
     */
    @Override
    public Pointer pcap_dump_open_append(final Pointer p, final String fname) {
      try {
        return NATIVE.pcap_dump_open_append(p, fname);
      } catch (final NullPointerException | UnsatisfiedLinkError e) {
        LOG.warn("pcap_dump_open_append: Function doesn't exist.");
        return null;
      }
    }

    /**
     * To be documented.
     *
     * @param p To be documented.
     * @param tstampType To be documented.
     * @return To be documented.
     */
    @Override
    public int pcap_set_tstamp_type(final Pointer p, final int tstampType) {
      try {
        return NATIVE.pcap_set_tstamp_type(p, tstampType);
      } catch (final NullPointerException | UnsatisfiedLinkError e) {
        LOG.warn("pcap_set_tstamp_type: Function doesn't exist.");
        return 0;
      }
    }

    /**
     * To be documented.
     *
     * @param p To be documented.
     * @return To be documented.
     */
    @Override
    public int pcap_get_tstamp_precision(final Pointer p) {
      try {
        return NATIVE.pcap_get_tstamp_precision(p);
      } catch (final NullPointerException | UnsatisfiedLinkError e) {
        LOG.warn("pcap_get_tstamp_precision: Function doesn't exist.");
        return Timestamp.Precision.MICRO.value();
      }
    }

    /**
     * To be documented.
     *
     * @param p To be documented.
     * @param rfmon To be documented.
     * @return To be documented.
     */
    @Override
    public int pcap_set_rfmon(final Pointer p, final int rfmon) {
      try {
        return NATIVE.pcap_set_rfmon(p, rfmon);
      } catch (final NullPointerException | UnsatisfiedLinkError e) {
        LOG.warn("pcap_set_rfmon: Function doesn't exist.");
        return 0;
      }
    }

    /**
     * To be documented.
     *
     * @param fname To be documented.
     * @param precision To be documented.
     * @param errbuf To be documented.
     * @return To be documented.
     */
    @Override
    public Pointer pcap_open_offline_with_tstamp_precision(final String fname, final int precision, final ErrorBuffer errbuf) {
      try {
        return NATIVE.pcap_open_offline_with_tstamp_precision(fname, precision, errbuf);
      } catch (final NullPointerException | UnsatisfiedLinkError e) {
        LOG.warn("pcap_open_offline_with_tstamp_precision: Function doesn't exist.");
        return null;
      }
    }

    /**
     * To be documented.
     *
     * @param p To be documented.
     * @param tstampPrecision To be documented.
     * @return To be documented.
     */
    @Override
    public int pcap_set_tstamp_precision(final Pointer p, final int tstampPrecision) {
      try {
        return NATIVE.pcap_set_tstamp_precision(p, tstampPrecision);
      } catch (final NullPointerException | UnsatisfiedLinkError e) {
        LOG.warn("pcap_set_tstamp_precision: Function doesn't exist.");
        return 0;
      }
    }

    /**
     * To be documented.
     *
     * @param p To be documented.
     * @param immediateMode To be documented.
     * @return To be documented.
     */
    @Override
    public int pcap_set_immediate_mode(final Pointer p, final int immediateMode) {
      try {
        return NATIVE.pcap_set_immediate_mode(p, immediateMode);
      } catch (final NullPointerException | UnsatisfiedLinkError e) {
        return 0; // ignore immediate mode for libpcap version before 1.5.0
      }
    }

    /**
     * To be documented.
     *
     * @param p To be documented.
     * @param size To be documented.
     * @return To be documented.
     */
    @Override
    public int pcap_setmintocopy(final Pointer p, final int size) {
      try {
        return NATIVE.pcap_setmintocopy(p, size);
      } catch (final NullPointerException | UnsatisfiedLinkError e) {
        LOG.warn("pcap_setmintocopy: Function doesn't exist.");
        return 0;
      }
    }

    /**
     * To be documented.
     *
     * @param opts To be documented.
     * @param errbuf To be documented.
     * @return To be documented.
     */
    @Override
    public int pcap_init(final int opts, final ErrorBuffer errbuf) {
      try {
        return NATIVE.pcap_init(opts, errbuf);
      } catch (final NullPointerException | UnsatisfiedLinkError e) {
        LOG.warn("pcap_init: Function doesn't exist.");
        return 0;
      }
    }

    /**
     * To be documented.
     *
     * @param p To be documented.
     * @return To be documented.
     */
    @Override
    public int pcap_get_selectable_fd(final Pointer p) {
      try {
        return NATIVE.pcap_get_selectable_fd(p);
      } catch (final NullPointerException | UnsatisfiedLinkError e) {
        throw new UnsupportedOperationException("pcap_get_selectable_fd: Function doesn't exist.");
      }
    }

    /**
     * To be documented.
     *
     * @param p To be documented.
     * @return To be documented.
     */
    @Override
    public Pointer pcap_get_required_select_timeout(final Pointer p) {
      try {
        return NATIVE.pcap_get_required_select_timeout(p);
      } catch (final NullPointerException | UnsatisfiedLinkError e) {
        LOG.warn("pcap_get_required_select_timeout: Function doesn't exist.");
        return null;
      }
    }

    /**
     * To be documented.
     *
     * @param p To be documented.
     * @return To be documented.
     */
    @Override
    public Handle pcap_getevent(final Pointer p) {
      try {
        return NATIVE.pcap_getevent(p);
      } catch (final NullPointerException | UnsatisfiedLinkError e) {
        throw new UnsupportedOperationException("pcap_getevent: Function doesn't exist.");
      }
    }
  }

  /**
   * To be documented.
   */
  public static final class ErrorBuffer extends Structure {
    /**
     * To be documented.
     */
    private byte[] buf;

    /**
     * To be documented.
     */
    public ErrorBuffer() {
      this(256);
    }

    /**
     * To be documented.
     *
     * @param size To be documented.
     */
    public ErrorBuffer(final int size) {
      this.buf = new byte[size];
    }

    /**
     * To be documented.
     *
     * @return To be documented.
     */
    @Override
    protected List<String> getFieldOrder() {
      return Collections.singletonList("buf");
    }

    /**
     * To be documented.
     *
     * @return To be documented.
     */
    @Override
    public String toString() {
      return com.sun.jna.Native.toString(buf);
    }
  }

  /**
   * To be documented.
   */
  public static final class BpfProgram extends Structure {

    /**
     * To be documented.
     */
    private int bfLen;
    /**
     * To be documented.
     */
    private BpfInsn.ByReference bfInsns;

    /**
     * To be documented.
     */
    public BpfProgram() {
      // public constructor
    }

    /**
     * To be documented.
     *
     * @return To be documented.
     */
    @Override
    protected List<String> getFieldOrder() {
      return Arrays.asList(
              "bf_len", //
              "bf_insns" //
      );
    }
  }

  /**
   * To be documented.
   */
  public static class BpfInsn extends Structure {

    /**
     * To be documented.
     */
    private short code;
    /**
     * To be documented.
     */
    private byte jt;
    /**
     * To be documented.
     */
    private byte jf;
    /**
     * To be documented.
     */
    private int k;

    /**
     * To be documented.
     */
    public BpfInsn() {
      // public constructor
    }

    /**
     * To be documented.
     *
     * @return To be documented.
     */
    @Override
    protected List<String> getFieldOrder() {
      return Arrays.asList(
              "code", //
              "jt", //
              "jf", //
              "k");
    }

    /**
     * To be documented.
     */
    public static final class ByReference extends BpfInsn implements Structure.ByReference {
    }
  }

  /**
   * To be documented.
   */
  public static class SockAddr extends Structure {

    /**
     * To be documented.
     */
    private static final ByteOrder NATIVE_BYTE_ORDER = ByteOrder.nativeOrder();
    /**
     * To be documented.
     */
    private short saFamily;
    /**
     * To be documented.
     */
    private byte[] saData = new byte[14];

    /**
     * To be documented.
     */
    public SockAddr() {
      // public constructor
    }

    /**
     * To be documented.
     *
     * @return To be documented.
     */
    static boolean isLinuxOrWindows() {
      return Platform.isWindows() || Platform.isLinux();
    }

    /**
     * To be documented.
     *
     * @param saFamily To be documented.
     * @param bo To be documented.
     * @return To be documented.
     */
    static short getSaFamilyByByteOrder(final short saFamily, final ByteOrder bo) {
      if (bo.equals(ByteOrder.BIG_ENDIAN)) {
        return (short) (0xFF & saFamily);
      } else {
        return (short) (0xFF & (saFamily >> 8));
      }
    }

    /**
     * To be documented.
     *
     * @return To be documented.
     */
    @Override
    protected List<String> getFieldOrder() {
      return Arrays.asList(
              "sa_family", //
              "sa_data" //
      );
    }

    /**
     * To be documented.
     *
     * @return To be documented.
     */
    public short getSaFamily() {
      if (isLinuxOrWindows()) {
        return saFamily;
      } else {
        return getSaFamilyByByteOrder(saFamily, NATIVE_BYTE_ORDER);
      }
    }

    /**
     * To be documented.
     */
    public static final class ByReference extends SockAddr implements Structure.ByReference {
    }
  }

  /**
   * To be documented.
   */
  public static class PcapIf extends Structure implements Interface {

    /**
     * To be documented.
     */
    private PcapIf.ByReference next;
    /**
     * To be documented.
     */
    private String name;
    /**
     * To be documented.
     */
    private String description;
    /**
     * To be documented.
     */
    private PcapAddr.ByReference addresses;
    /**
     * To be documented.
     */
    private int flags;

    /**
     * To be documented.
     */
    public PcapIf() {
      // public constructor
    }

    /**
     * To be documented.
     *
     * @param pointer To be documented.
     */
    public PcapIf(final Pointer pointer) {
      super(pointer);
      read();
    }

    /**
     * To be documented.
     *
     * @return To be documented.
     */
    @Override
    public PcapIf next() {
      return next;
    }

    /**
     * To be documented.
     *
     * @return To be documented.
     */
    @Override
    public String name() {
      return name;
    }

    /**
     * To be documented.
     *
     * @return To be documented.
     */
    @Override
    public String description() {
      return description;
    }

    /**
     * To be documented.
     *
     * @return To be documented.
     */
    @Override
    public PcapAddr addresses() {
      return addresses;
    }

    /**
     * To be documented.
     *
     * @return To be documented.
     */
    @Override
    public int flags() {
      return flags;
    }

    /**
     * To be documented.
     *
     * @return To be documented.
     */
    @Override
    public Iterator<Interface> iterator() {
      return new DefaultInterfaceIterator(this);
    }

    /**
     * To be documented.
     *
     * @return To be documented.
     */
    @Override
    protected List<String> getFieldOrder() {
      return Arrays.asList(
              "next", //
              "name", //
              "description", //
              "addresses", //
              "flags" //
      );
    }

    /**
     * To be documented.
     */
    public static final class ByReference extends PcapIf implements Structure.ByReference {
    }
  }

  /**
   * To be documented.
   */
  public static class PcapAddr extends Structure implements Address {

    /**
     * To be documented.
     */
    private PcapAddr.ByReference next;
    /**
     * To be documented.
     */
    private SockAddr.ByReference addr;
    /**
     * To be documented.
     */
    private SockAddr.ByReference netmask;
    /**
     * To be documented.
     */
    private SockAddr.ByReference broadaddr;
    /**
     * To be documented.
     */
    private SockAddr.ByReference dstaddr;

    /**
     * To be documented.
     */
    public PcapAddr() {
    }

    /**
     * To be documented.
     *
     * @return To be documented.
     */
    @Override
    public PcapAddr next() {
      return next;
    }

    /**
     * To be documented.
     *
     * @return To be documented.
     */
    @Override
    public InetAddress address() {
      return inetAddress(addr);
    }

    /**
     * To be documented.
     *
     * @return To be documented.
     */
    @Override
    public InetAddress netmask() {
      return inetAddress(netmask);
    }

    /**
     * To be documented.
     *
     * @return To be documented.
     */
    @Override
    public InetAddress broadcast() {
      return inetAddress(broadaddr);
    }

    /**
     * To be documented.
     *
     * @return To be documented.
     */
    @Override
    public InetAddress destination() {
      return inetAddress(dstaddr);
    }

    /**
     * To be documented.
     *
     * @return To be documented.
     */
    @Override
    public Iterator<Address> iterator() {
      return new DefaultAddressIterator(this);
    }

    /**
     * To be documented.
     *
     * @return To be documented.
     */
    @Override
    protected List<String> getFieldOrder() {
      return Arrays.asList(
              "next", //
              "addr", //
              "netmask", //
              "broadaddr", //
              "dstaddr" //
      );
    }

    /**
     * To be documented.
     */
    public static final class ByReference extends PcapAddr implements Structure.ByReference {
    }
  }

  /**
   * To be documented.
   */
  public static class Handle extends PointerType {

    /**
     * To be documented.
     */
    private static Handle invalidHandleValue = new Handle(Pointer.createConstant(Native.POINTER_SIZE == 8 ? -1 : 0xFFFFFFFFL));
    /**
     * To be documented.
     */
    private boolean immutable;

    /**
     * To be documented.
     */
    public Handle() {
    }

    /**
     * To be documented.
     *
     * @param p To be documented.
     */
    public Handle(final Pointer p) {
      super(p);
      immutable = true;
    }

    /**
     * Override to the appropriate object for INVALID_HANDLE_VALUE.
     */
    @Override
    public Object fromNative(final Object nativeValue, final FromNativeContext context) {
      final Object o = super.fromNative(nativeValue, context);
      if (Handle.invalidHandleValue.equals(o)) {
        return Handle.invalidHandleValue;
      }
      return o;
    }

    /**
     * To be documented.
     *
     * @param p To be documented.
     */
    @Override
    public void setPointer(final Pointer p) {
      if (immutable) {
        throw new UnsupportedOperationException("immutable reference");
      }
      super.setPointer(p);
    }

    /**
     * To be documented.
     *
     * @param o To be documented.
     * @return To be documented.
     */
    @Override
    public boolean equals(final Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }
      final Handle handle = (Handle) o;
      return Pointer.nativeValue(getPointer()) == Pointer.nativeValue(handle.getPointer());
    }

    /**
     * To be documented.
     *
     * @return To be documented.
     */
    @Override
    public int hashCode() {
      return Objects.hash(Pointer.nativeValue(getPointer()));
    }

    /**
     * To be documented.
     *
     * @return To be documented.
     */
    @Override
    public String toString() {
      return String.valueOf(getPointer());
    }
  }

  /**
   * To be documented.
   */
  public static class PcapPkthdr extends Structure {

    /**
     * To be documented.
     */
    public static final int TS_OFFSET;
    /**
     * To be documented.
     */
    public static final int CAPLEN_OFFSET;
    /**
     * To be documented.
     */
    public static final int LEN_OFFSET;

    static {
      final PcapPkthdr ph = new PcapPkthdr();
      TS_OFFSET = ph.fieldOffset("ts");
      CAPLEN_OFFSET = ph.fieldOffset("caplen");
      LEN_OFFSET = ph.fieldOffset("len");
    }

    /**
     * To be documented.
     */
    @AllowPublicAccess
    public TimeVal ts; // struct TimeVal
    /**
     * To be documented.
     */
    @AllowPublicAccess
    public int caplen; // bpf_u_int32
    /**
     * To be documented.
     */
    @AllowPublicAccess
    public int len; // bpf_u_int32

    /**
     * To be documented.
     */
    public PcapPkthdr() {
    }

    /**
     * To be documented.
     *
     * @param p To be documented.
     */
    public PcapPkthdr(final Pointer p) {
      super(p);
      read();
    }

    static NativeLong getTvSec(final Pointer p) {
      return p.getNativeLong(TS_OFFSET + TimeVal.TV_SEC_OFFSET);
    }

    static NativeLong getTvUsec(final Pointer p) {
      return p.getNativeLong(TS_OFFSET + TimeVal.TV_USEC_OFFSET);
    }

    static int getCaplen(final Pointer p) {
      return p.getInt(CAPLEN_OFFSET);
    }

    static int getLen(final Pointer p) {
      return p.getInt(LEN_OFFSET);
    }

    /**
     * To be documented.
     *
     * @return To be documented.
     */
    @Override
    protected List<String> getFieldOrder() {
      final List<String> list = new ArrayList<String>();
      list.add("ts");
      list.add("caplen");
      list.add("len");
      return list;
    }

    /**
     * To be documented.
     */
    public static class ByReference extends PcapPkthdr implements Structure.ByReference {
    }
  }

  /**
   * To be documented.
   */
  public static class TimeVal extends Structure {

    /**
     * To be documented.
     */
    public static final int TV_SEC_OFFSET;
    /**
     * To be documented.
     */
    public static final int TV_USEC_OFFSET;

    static {
      final TimeVal tv = new TimeVal();
      TV_SEC_OFFSET = tv.fieldOffset("tvSec");
      TV_USEC_OFFSET = tv.fieldOffset("tvUsec");
    }

    /**
     * To be documented.
     */
    @AllowPublicAccess
    public NativeLong tvSec; // long
    /**
     * To be documented.
     */
    @AllowPublicAccess
    public NativeLong tvUsec; // long

    /**
     * To be documented.
     */
    public TimeVal() {
    }

    /**
     * To be documented.
     *
     * @return To be documented.
     */
    @Override
    protected List<String> getFieldOrder() {
      final List<String> list = new ArrayList<String>();
      list.add("tvSec");
      list.add("tvUsec");
      return list;
    }
  }
}

