//package com.github.waifu.algorithms;
//
//import ca.pfv.spmf.algorithms.ArraysAlgos;
//import ca.pfv.spmf.patterns.itemset_array_integers_with_count.Itemset;
//import ca.pfv.spmf.patterns.itemset_array_integers_with_count.Itemsets;
//import ca.pfv.spmf.tools.MemoryLogger;
//import java.io.BufferedReader;
//import java.io.BufferedWriter;
//import java.io.FileReader;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Collection;
//import java.util.Collections;
//import java.util.Comparator;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//
///**
// * To be documented.
// */
//public class AlgoAprioriClose {
//  /**
//   * To be documented.
//   */
//  private int k;
//  /**
//   * To be documented.
//   */
//  private int totalCandidateCount = 0;
//  /**
//   * To be documented.
//   */
//  private long startTimestamp;
//  /**
//   * To be documented.
//   */
//  private long endTimestamp;
//  /**
//   * To be documented.
//   */
//  private int itemsetCount;
//  /**
//   * To be documented.
//   */
//  private int databaseSize;
//  /**
//   * To be documented.
//   */
//  private int minsupRelative;
//  /**
//   * To be documented.
//   */
//  public List<int[]> database = null;
//  /**
//   * To be documented.
//   */
//  private Itemsets patterns = null;
//  /**
//   * To be documented.
//   */
//  private BufferedWriter writer = null;
//  /**
//   * To be documented.
//   */
//  public Map<String, Integer> names = new HashMap<>();
//
//  /**
//   * To be documented.
//   */
//  public AlgoAprioriClose() {
//  }
//
//  /**
//   * To be documented.
//   *
//   * @param minsup To be documented.
//   * @param input To be documented.
//   * @param output To be documented.
//   * @return To be documented.
//   * @throws IOException To be documented.
//   */
//  public Itemsets runAlgorithm(final double minsup, final String input, final String output) throws IOException {
//    if (output == null) {
//      this.writer = null;
//      this.patterns = new Itemsets("FREQUENT ITEMSETS");
//    } else {
//      this.patterns = null;
//      this.writer = new BufferedWriter(new FileWriter(output));
//    }
//
//    this.startTimestamp = System.currentTimeMillis();
//    this.itemsetCount = 0;
//    this.totalCandidateCount = 0;
//    MemoryLogger.getInstance().reset();
//    this.databaseSize = 0;
//    Map<Integer, Integer> mapItemCount = new HashMap();
//    this.database = new ArrayList();
//    BufferedReader reader = new BufferedReader(new FileReader(input));
//
//    while (true) {
//      String line;
//      do {
//        do {
//          do {
//            do {
//              if ((line = reader.readLine()) == null) {
//                reader.close();
//                this.minsupRelative = (int) Math.ceil(minsup * (double) this.databaseSize);
//                this.k = 1;
//                List<Integer> frequent1 = new ArrayList();
//                Iterator previousLevel = mapItemCount.entrySet().iterator();
//
//                while (previousLevel.hasNext()) {
//                  Map.Entry<Integer, Integer> entry = (Map.Entry) previousLevel.next();
//                  if ((Integer) entry.getValue() >= this.minsupRelative) {
//                    frequent1.add((Integer) entry.getKey());
//                  }
//                }
//
//                Collections.sort(frequent1, new Comparator<Integer>() {
//                  public int compare(final Integer o1, final Integer o2) {
//                    return o1 - o2;
//                  }
//                });
//                if (frequent1.size() == 0) {
//                  if (this.writer != null) {
//                    this.writer.close();
//                  }
//
//                  return this.patterns;
//                }
//
//                this.totalCandidateCount += frequent1.size();
//                List<Itemset> level = null;
//                previousLevel = null;
//                this.k = 2;
//
//                do {
//                  MemoryLogger.getInstance().checkMemory();
//                  List candidatesK;
//                  if (this.k == 2) {
//                    candidatesK = this.generateCandidate2(frequent1);
//                  } else {
//                    candidatesK = this.generateCandidateSizeK(level);
//                  }
//
//                  this.totalCandidateCount += candidatesK.size();
//                  Iterator var13 = this.database.iterator();
//
//                  label92:
//                  while (var13.hasNext()) {
//                    int[] transaction = (int[]) var13.next();
//                    Iterator var15 = candidatesK.iterator();
//
//                    while (true) {
//                      while (true) {
//                        if (!var15.hasNext()) {
//                          continue label92;
//                        }
//
//                        Itemset candidate = (Itemset) var15.next();
//                        int pos = 0;
//                        int[] var20 = transaction;
//                        int var19 = transaction.length;
//
//                        for (int var18 = 0; var18 < var19; ++var18) {
//                          int item = var20[var18];
//                          if (item == candidate.itemset[pos]) {
//                            ++pos;
//                            if (pos == candidate.itemset.length) {
//                              ++candidate.support;
//                              break;
//                            }
//                          } else if (item > candidate.itemset[pos]) {
//                            break;
//                          }
//                        }
//                      }
//                    }
//                  }
//
//                  final List<Itemset> prevLevel = level;
//                  level = new ArrayList();
//                  var13 = candidatesK.iterator();
//
//                  while (var13.hasNext()) {
//                    Itemset candidate = (Itemset) var13.next();
//                    if (candidate.getAbsoluteSupport() >= this.minsupRelative) {
//                      level.add(candidate);
//                    }
//                  }
//
//                  if (prevLevel == null) {
//                    this.checkIfItemsetsK_1AreClosed(frequent1, level, mapItemCount);
//                  } else {
//                    this.checkIfItemsetsK_1AreClosed(prevLevel, level);
//                  }
//
//                  ++this.k;
//                } while (!level.isEmpty());
//
//                this.endTimestamp = System.currentTimeMillis();
//                MemoryLogger.getInstance().checkMemory();
//                if (this.writer != null) {
//                  this.writer.close();
//                }
//
//                return this.patterns;
//              }
//            } while (line.isEmpty());
//          } while (line.charAt(0) == '#');
//        } while (line.charAt(0) == '%');
//      } while (line.charAt(0) == '@');
//
//      String[] lineSplited = line.split(" ");
//      int[] transaction = new int[lineSplited.length];
//
//      for (int i = 0; i < lineSplited.length; ++i) {
//        if (!names.containsKey(lineSplited[i])) {
//          names.put(lineSplited[i], names.size() + 1);
//        }
//        Integer item = names.get(lineSplited[i]);
//        transaction[i] = item;
//        Integer count = (Integer) mapItemCount.get(item);
//        if (count == null) {
//          mapItemCount.put(item, 1);
//        } else {
//          mapItemCount.put(item, count + 1);
//        }
//      }
//
//      Arrays.sort(transaction);
//      System.out.println(Arrays.toString(transaction));
//
//      this.database.add(transaction);
//      ++this.databaseSize;
//    }
//  }
//
//  /**
//   * To be documented.
//   *
//   * @param levelKm1 To be documented.
//   * @param levelK To be documented.
//   * @throws IOException To be documented.
//   */
//  private void checkIfItemsetsK_1AreClosed(final Collection<Itemset> levelKm1, final List<Itemset> levelK) throws IOException {
//    Iterator var4 = levelKm1.iterator();
//
//    while (var4.hasNext()) {
//      Itemset itemset = (Itemset) var4.next();
//      boolean isClosed = true;
//      Iterator var7 = levelK.iterator();
//
//      while (var7.hasNext()) {
//        Itemset itemsetK = (Itemset) var7.next();
//        if (itemsetK.getAbsoluteSupport() == itemset.getAbsoluteSupport() && itemsetK.containsAll(itemset)) {
//          isClosed = false;
//          break;
//        }
//      }
//
//      if (isClosed) {
//        this.saveItemset(itemset);
//      }
//    }
//
//  }
//
//  /**
//   * To be documented.
//   *
//   * @param levelKm1 To be documented.
//   * @param levelK To be documented.
//   * @param mapItemCount To be documented.
//   * @throws IOException To be documented.
//   */
//  private void checkIfItemsetsK_1AreClosed(final List<Integer> levelKm1, final List<Itemset> levelK, final Map<Integer, Integer> mapItemCount) throws IOException {
//    Iterator var5 = levelKm1.iterator();
//
//    while (var5.hasNext()) {
//      Integer itemset = (Integer) var5.next();
//      boolean isClosed = true;
//      int support = (Integer) mapItemCount.get(itemset);
//      Iterator var9 = levelK.iterator();
//
//      while (var9.hasNext()) {
//        Itemset itemsetK = (Itemset) var9.next();
//        if (itemsetK.getAbsoluteSupport() == support && itemsetK.contains(itemset)) {
//          isClosed = false;
//          break;
//        }
//      }
//
//      if (isClosed) {
//        this.saveItemset(itemset, support);
//      }
//    }
//
//  }
//
//  /**
//   * To be documented.
//   *
//   * @return To be documented.
//   */
//  public int getDatabaseSize() {
//    return this.databaseSize;
//  }
//
//  /**
//   * To be documented.
//   *
//   * @param frequent1 To be documented.
//   * @return To be documented.
//   */
//  private List<Itemset> generateCandidate2(final List<Integer> frequent1) {
//    List<Itemset> candidates = new ArrayList();
//
//    for (int i = 0; i < frequent1.size(); ++i) {
//      Integer item1 = (Integer) frequent1.get(i);
//
//      for (int j = i + 1; j < frequent1.size(); ++j) {
//        Integer item2 = (Integer) frequent1.get(j);
//        candidates.add(new Itemset(new int[]{item1, item2}));
//      }
//    }
//
//    return candidates;
//  }
//
//  /**
//   * To be documented.
//   *
//   * @param levelK1 To be documented.
//   * @return To be documented.
//   */
//  protected List<Itemset> generateCandidateSizeK(final List<Itemset> levelK1) {
//    List<Itemset> candidates = new ArrayList();
//
//    label44:
//    for (int i = 0; i < levelK1.size(); ++i) {
//      int[] itemset1 = ((Itemset) levelK1.get(i)).itemset;
//
//      label41:
//      for (int j = i + 1; j < levelK1.size(); ++j) {
//        int[] itemset2 = ((Itemset) levelK1.get(j)).itemset;
//
//        for (int k = 0; k < itemset1.length; ++k) {
//          if (k == itemset1.length - 1) {
//            if (itemset1[k] >= itemset2[k]) {
//              continue label44;
//            }
//          } else {
//            if (itemset1[k] < itemset2[k]) {
//              continue label41;
//            }
//
//            if (itemset1[k] > itemset2[k]) {
//              continue label44;
//            }
//          }
//        }
//
//        int[] newItemset = new int[itemset1.length + 1];
//        System.arraycopy(itemset1, 0, newItemset, 0, itemset1.length);
//        newItemset[itemset1.length] = itemset2[itemset2.length - 1];
//        if (this.allSubsetsOfSizeK_1AreFrequent(newItemset, levelK1)) {
//          candidates.add(new Itemset(newItemset));
//        }
//      }
//    }
//
//    return candidates;
//  }
//
//  /**
//   * To be documented.
//   *
//   * @param candidate To be documented.
//   * @param levelK1 To be documented.
//   * @return To be documented.
//   */
//  protected boolean allSubsetsOfSizeK_1AreFrequent(final int[] candidate, final List<Itemset> levelK1) {
//    for (int posRemoved = 0; posRemoved < candidate.length; ++posRemoved) {
//      int first = 0;
//      int last = levelK1.size() - 1;
//      boolean found = false;
//
//      while (first <= last) {
//        int middle = first + last >>> 1;
//        int comparison = ArraysAlgos.sameAs(((Itemset) levelK1.get(middle)).getItems(), candidate, posRemoved);
//        if (comparison < 0) {
//          first = middle + 1;
//        } else {
//          if (comparison <= 0) {
//            found = true;
//            break;
//          }
//
//          last = middle - 1;
//        }
//      }
//
//      if (!found) {
//        return false;
//      }
//    }
//
//    return true;
//  }
//
//  /**
//   * To be documented.
//   *
//   * @param itemset To be documented.
//   * @throws IOException To be documented.
//   */
//  void saveItemset(final Itemset itemset) throws IOException {
//    ++this.itemsetCount;
//    if (this.writer != null) {
//      this.writer.write(itemset.toString() + " #SUP: " + itemset.getAbsoluteSupport());
//      this.writer.newLine();
//    } else {
//      this.patterns.addItemset(itemset, itemset.size());
//    }
//
//  }
//
//  /**
//   * To be documented.
//   *
//   * @param item To be documented.
//   * @param support To be documented.
//   * @throws IOException To be documented.
//   */
//  void saveItemset(final Integer item, final Integer support) throws IOException {
//    ++this.itemsetCount;
//    if (this.writer != null) {
//      this.writer.write(item + " #SUP: " + support);
//      this.writer.newLine();
//    } else {
//      Itemset itemset = new Itemset(item);
//      itemset.setAbsoluteSupport(support);
//      this.patterns.addItemset(itemset, 1);
//    }
//
//  }
//
//  /**
//   * To be documented.
//   */
//  public void printStats() {
//    System.out.println("=============  APRIORI - STATS =============");
//    System.out.println(" Candidates count : " + this.totalCandidateCount);
//    System.out.println(" The algorithm stopped at size " + (this.k - 1) + ", because there is no candidate");
//    System.out.println(" Frequent closed itemsets count : " + this.itemsetCount);
//    System.out.println(" Maximum memory usage : " + MemoryLogger.getInstance().getMaxMemory() + " mb");
//    System.out.println(" Total time ~ " + (this.endTimestamp - this.startTimestamp) + " ms");
//    System.out.println("===================================================");
//  }
//}
