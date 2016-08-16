package com.game.hacking.app.hackinggame2d.game;

/**
 * Created by MAGarcia on 8/11/2016.
 */
public class NetworkNode implements Node, Comparable<NetworkNode> {
  private static final String  DEFAULT_NETWORK_NAME = "Default";
  private Integer clients;
  private Integer password;
  private Integer networkId;
  private Integer networkIp;
  private String networkName;

  @Override
  public int compareTo(NetworkNode networkNode) {
    return networkId.compareTo(networkNode.networkId);
  }

  public enum PasswordStrength {
    NONE,
    LOW,
    LOW_MEDIUM,
    MEDIUM,
    HIGH_MEDIUM,
    HIGH,
    NEAR_IMPOSSIBLE,
    IMPOSSIBLE,
  }

  private PasswordStrength strength;

  public Integer getClients() {
    return clients;
  }

  public void setClients(Integer clients) {
    this.clients = clients;
  }

  public Integer getNetworkId() {
    return networkId;
  }

  public void setNetworkId(Integer networkId) {
    this.networkId = networkId;
  }

  public Integer getNetworkIp() {
    return networkIp;
  }

  public void setNetworkIp(Integer networkIp) {
    this.networkIp = networkIp;
  }

  public String getNetworkName() {
    return networkName;
  }

  public void setNetworkName(String networkName) {
    this.networkName = networkName;
  }

  public PasswordStrength getStrength() {
    return strength;
  }

  public void setStrength(PasswordStrength strength) {
    this.strength = strength;
  }


  public NetworkNode(String networkName, Integer networkIp, Integer networkId, Integer clients,
                     Integer password)
  {
    this.clients = clients;
    this.networkId = networkId;
    this.networkIp = networkIp;
    this.networkName = networkName;
    this.password = password;
    determinePasswordStrength();
  }

  public NetworkNode() {
    this (DEFAULT_NETWORK_NAME, 0, 0, 0, 0);
  }

  private void determinePasswordStrength() {
    if (password <= 0) {
      strength = PasswordStrength.NONE;
    } else if (password <= 1500) {
      strength = PasswordStrength.LOW;
    } else if (password <= 5050) {
      strength = PasswordStrength.LOW_MEDIUM;
    } else if (password <= 10020) {
      strength = PasswordStrength.MEDIUM;
    } else if (password <= 15000) {
      strength = PasswordStrength.HIGH_MEDIUM;
    } else if (password <= 20001) {
      strength = PasswordStrength.HIGH;
    } else if (password <= 25003) {
      strength = PasswordStrength.NEAR_IMPOSSIBLE;
    } else {
      strength = PasswordStrength.IMPOSSIBLE;
    }
  }
}
