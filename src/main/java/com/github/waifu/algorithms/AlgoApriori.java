package com.github.waifu.algorithms;

import ca.pfv.spmf.algorithms.ArraysAlgos;
import ca.pfv.spmf.patterns.itemset_array_integers_with_count.Itemset;
import ca.pfv.spmf.patterns.itemset_array_integers_with_count.Itemsets;
import ca.pfv.spmf.tools.MemoryLogger;
import com.github.waifu.handlers.DatabaseHandler;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.json.JSONArray;

/**
 * To be documented.
 */
public class AlgoApriori {
  /**
   * To be documented.
   */
  private int k;
  /**
   * To be documented.
   */
  private int totalCandidateCount = 0;
  /**
   * To be documented.
   */
  private long startTimestamp;
  /**
   * To be documented.
   */
  private long endTimestamp;
  /**
   * To be documented.
   */
  private int itemsetCount;
  /**
   * To be documented.
   */
  private int databaseSize;
  /**
   * To be documented.
   */
  private int minsupRelative;
  /**
   * To be documented.
   */
  private List<int[]> database = null;
  /**
   * To be documented.
   */
  private Itemsets patterns = null;
  /**
   * To be documented.
   */
  private BufferedWriter writer = null;
  /**
   * To be documented.
   */
  private int maxPatternLength = 10000;
  /**
   * To be documented.
   */
  public Map<String, Integer> names = new HashMap<>();

  /**
   * To be documented.
   */
  public AlgoApriori() {
  }

  /**
   * To be documented.
   *
   * @param minsup To be documented.
   * @param input To be documented.
   * @param output To be documented.
   * @return To be documented.
   * @throws IOException To be documented.
   */
  public Itemsets runAlgorithm(final double minsup, final String input, final String output) throws IOException {
    if (output == null) {
      this.writer = null;
      this.patterns = new Itemsets("FREQUENT ITEMSETS");
    } else {
      this.patterns = null;
      this.writer = new BufferedWriter(new FileWriter(output));
    }

    this.startTimestamp = System.currentTimeMillis();
    this.itemsetCount = 0;
    this.totalCandidateCount = 0;
    MemoryLogger.getInstance().reset();
    this.databaseSize = 0;
    Map<Integer, Integer> mapItemCount = new HashMap();
    this.database = new ArrayList();
    JSONArray jsonArray = DatabaseHandler.getAll();

    StringBuilder in = new StringBuilder();
    for (int i = 0; i < jsonArray.length(); i++) {
      in.append(jsonArray.getJSONObject(i).getString("raiders"));
      if (i != jsonArray.length() - 1) {
        in.append("\n");
      }
    }
    in.append("\n");
    BufferedReader reader = new BufferedReader(new StringReader(in.toString()));

    while (true) {
      String line;
      do {
        do {
          do {
            do {
              if ((line = reader.readLine()) == null) {
                this.minsupRelative = (int) Math.ceil(minsup * (double) this.databaseSize);
                this.k = 1;
                List<Integer> frequent1 = new ArrayList();
                Iterator var23 = mapItemCount.entrySet().iterator();

                while (var23.hasNext()) {
                  Entry<Integer, Integer> entry = (Entry) var23.next();
                  if ((Integer) entry.getValue() >= this.minsupRelative) {
                    frequent1.add((Integer) entry.getKey());
                    this.saveItemsetToFile((Integer) entry.getKey(), (Integer) entry.getValue());
                  }
                }

                mapItemCount = null;
                Collections.sort(frequent1, new Comparator<Integer>() {
                  public int compare(final Integer o1, final Integer o2) {
                    return o1 - o2;
                  }
                });
                if (frequent1.size() != 0 && this.maxPatternLength > 1) {
                  this.totalCandidateCount += frequent1.size();
                  List<Itemset> level = null;
                  this.k = 2;

                  label104:
                  do {
                    MemoryLogger.getInstance().checkMemory();
                    List candidatesK;
                    if (this.k == 2) {
                      candidatesK = this.generateCandidate2(frequent1);
                    } else {
                      candidatesK = this.generateCandidateSizeK(level);
                    }

                    this.totalCandidateCount += candidatesK.size();
                    Iterator var27 = this.database.iterator();

                    label93:
                    while (true) {
                      int[] transaction;
                      do {
                        if (!var27.hasNext()) {
                          level = new ArrayList();
                          if (this.k < this.maxPatternLength + 1) {
                            var27 = candidatesK.iterator();

                            while (var27.hasNext()) {
                              Itemset candidate = (Itemset) var27.next();
                              if (candidate.getAbsoluteSupport() >= this.minsupRelative) {
                                level.add(candidate);
                                this.saveItemset(candidate);
                              }
                            }
                          }

                          ++this.k;
                          continue label104;
                        }

                        transaction = (int[]) var27.next();
                      } while (transaction.length < this.k);

                      Iterator var14 = candidatesK.iterator();

                      while (true) {
                        while (true) {
                          if (!var14.hasNext()) {
                            continue label93;
                          }

                          Itemset candidate = (Itemset) var14.next();
                          int pos = 0;
                          int[] var19 = transaction;
                          int var18 = transaction.length;

                          for (int var17 = 0; var17 < var18; ++var17) {
                            int item = var19[var17];
                            if (item == candidate.itemset[pos]) {
                              ++pos;
                              if (pos == candidate.itemset.length) {
                                ++candidate.support;
                                break;
                              }
                            } else if (item > candidate.itemset[pos]) {
                              break;
                            }
                          }
                        }
                      }
                    }
                  } while (!level.isEmpty());

                  this.endTimestamp = System.currentTimeMillis();
                  MemoryLogger.getInstance().checkMemory();
                  if (this.writer != null) {
                    this.writer.close();
                  }
                  return this.patterns;
                }

                this.endTimestamp = System.currentTimeMillis();
                MemoryLogger.getInstance().checkMemory();
                if (this.writer != null) {
                  this.writer.close();
                }

                return this.patterns;
              }
            } while (line.isEmpty());
          } while (line.charAt(0) == '#');
        } while (line.charAt(0) == '%');
      } while (line.charAt(0) == '@');

      String[] lineSplited = line.split(" ");

      int[] transaction = new int[lineSplited.length];

      for (int i = 0; i < lineSplited.length; ++i) {
        if (!names.containsKey(lineSplited[i])) {
          names.put(lineSplited[i], names.size() + 1);
        }
        Integer item = names.get(lineSplited[i]);
        transaction[i] = item;
        Integer count = (Integer) mapItemCount.get(item);
        if (count == null) {
          mapItemCount.put(item, 1);
        } else {
          mapItemCount.put(item, count + 1);
        }
      }

      Arrays.sort(transaction);
      System.out.println(Arrays.toString(transaction));

      this.database.add(transaction);
      ++this.databaseSize;
    }
  }

  /**
   * To be documented.
   *
   * @return To be documented.
   */
  public int getDatabaseSize() {
    return this.databaseSize;
  }

  private List<Itemset> generateCandidate2(final List<Integer> frequent1) {
    List<Itemset> candidates = new ArrayList();

    for (int i = 0; i < frequent1.size(); ++i) {
      Integer item1 = (Integer) frequent1.get(i);

      for (int j = i + 1; j < frequent1.size(); ++j) {
        Integer item2 = (Integer) frequent1.get(j);
        candidates.add(new Itemset(new int[]{item1, item2}));
      }
    }

    return candidates;
  }

  /**
   * To be documented.
   *
   * @param levelK1 To be documented.
   * @return To be documented.
   */
  protected List<Itemset> generateCandidateSizeK(final List<Itemset> levelK1) {
    List<Itemset> candidates = new ArrayList();

    label44:
    for (int i = 0; i < levelK1.size(); ++i) {
      int[] itemset1 = ((Itemset) levelK1.get(i)).itemset;

      label41:
      for (int j = i + 1; j < levelK1.size(); ++j) {
        int[] itemset2 = ((Itemset) levelK1.get(j)).itemset;

        for (int k = 0; k < itemset1.length; ++k) {
          if (k == itemset1.length - 1) {
            if (itemset1[k] >= itemset2[k]) {
              continue label44;
            }
          } else {
            if (itemset1[k] < itemset2[k]) {
              continue label41;
            }

            if (itemset1[k] > itemset2[k]) {
              continue label44;
            }
          }
        }

        int[] newItemset = new int[itemset1.length + 1];
        System.arraycopy(itemset1, 0, newItemset, 0, itemset1.length);
        newItemset[itemset1.length] = itemset2[itemset2.length - 1];
        if (this.allSubsetsOfSizeK_1AreFrequent(newItemset, levelK1)) {
          candidates.add(new Itemset(newItemset));
        }

      }
    }

    return candidates;
  }

  /**
   * To be documented.
   *
   * @param candidate To be documented.
   * @param levelK1 To be documented.
   * @return To be documented.
   */
  protected boolean allSubsetsOfSizeK_1AreFrequent(final int[] candidate, final List<Itemset> levelK1) {
    for (int posRemoved = 0; posRemoved < candidate.length; ++posRemoved) {
      int first = 0;
      int last = levelK1.size() - 1;
      boolean found = false;

      while (first <= last) {
        int middle = first + last >> 1;
        int comparison = ArraysAlgos.sameAs(((Itemset) levelK1.get(middle)).getItems(), candidate, posRemoved);
        if (comparison < 0) {
          first = middle + 1;
        } else {
          if (comparison <= 0) {
            found = true;
            break;
          }

          last = middle - 1;
        }
      }

      if (!found) {
        return false;
      }
    }

    return true;
  }

  /**
   * To be documented.
   *
   * @param itemset To be documented.
   * @throws IOException To be documented.
   */
  void saveItemset(final Itemset itemset) throws IOException {
    ++this.itemsetCount;
    if (this.writer != null) {
      this.writer.write(itemset.toString() + " #SUP: " + itemset.getAbsoluteSupport());
      this.writer.newLine();
    } else {
      this.patterns.addItemset(itemset, itemset.size());
    }

  }

  /**
   * To be documented.
   *
   * @param item To be documented.
   * @param support To be documented.
   * @throws IOException To be documented.
   */
  void saveItemsetToFile(final Integer item, final Integer support) throws IOException {
    ++this.itemsetCount;
    if (this.writer != null) {
      this.writer.write(item + " #SUP: " + support);
      this.writer.newLine();
    } else {
      Itemset itemset = new Itemset(item);
      itemset.setAbsoluteSupport(support);
      this.patterns.addItemset(itemset, 1);
    }

  }

  /**
   * To be documented.
   */
  public void printStats() {
    System.out.println("=============  APRIORI - STATS =============");
    System.out.println(" Candidates count : " + this.totalCandidateCount);
    System.out.println(" The algorithm stopped at size " + (this.k - 1));
    System.out.println(" Frequent itemsets count : " + this.itemsetCount);
    System.out.println(" Maximum memory usage : " + MemoryLogger.getInstance().getMaxMemory() + " mb");
    System.out.println(" Total time ~ " + (this.endTimestamp - this.startTimestamp) + " ms");
    System.out.println("===================================================");
  }

  /**
   * To be documented.
   *
   * @param length To be documented.
   */
  public void setMaximumPatternLength(final int length) {
    this.maxPatternLength = length;
  }
}
