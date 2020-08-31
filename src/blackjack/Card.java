package blackjack;

import java.io.Serializable;

public class Card implements Serializable {
 private String name;
 private double value;
 private String suit;
 private int owner;

 public Card(String name, double value, String suit, int owner) {
  this.name = name;
  this.value = value;
  this.suit = suit;
  this.owner = owner;
 }

 public String getName() {
  return name;
 }

 public void setName(String name) {
  this.name = name;
 }

 public double getValue() {
  return value;
 }

 public void setValue(double value) {
  this.value = value;
 }

 public String getSuit() {
  return suit;
 }

 public void setSuit(String suit) {
  this.suit = suit;
 }

 public int getOwner() {
  return owner;
 }

 public void setOwner(int owner) {
  this.owner = owner;
 }

}
