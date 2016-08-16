package com.game.hacking.app.hackinggame2d.mapsequence;

import java.io.Serializable;

/**
 * Tree interface which is used to develop a workable tree data structure.
 * @param <K> The key used to store the associated Value.
 * @param <V> The value that is to be stored with its key associate.
 * @author MAGarcia
 */
public interface Tree<K extends Comparable<K>, V> extends Serializable
{
  /**
   * Adds the key, value pair into the tree.
   * @param key The key used that is associated with the value.
   * @param value The data that is to be stored.
   */
  public void add(K key, V value);

  /**
   * Removes the key,value pair.
   * @param key The key used to find the value and remove.
   * @return The value associated with the key prior to removal.
   */
  public V remove(K key);

  /**
   * Looks up the value associated with the key.
   * @param key The key used to find the associated value of.
   * @return The value associated with the key.
   */
  public V lookup(K key);

  /**
   * Supposed to print a pyramid like string of the data in the tree.
   * @return The string format pyramid like form of data in the tree.
   */
  public String toPrettyString();
}
