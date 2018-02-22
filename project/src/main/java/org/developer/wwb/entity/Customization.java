package org.developer.wwb.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Customization implements Serializable{
	private static final long serialVersionUID = 1708395126145722805L;
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	@Column(nullable = false)
	private String name;
	@Column(nullable = false)
	private String value;
	private String extend1;
	private String extend2;
	private String extend3;
	private String extend4;
	private String extend5;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getExtend1() {
		return extend1;
	}
	public void setExtend1(String extend1) {
		this.extend1 = extend1;
	}
	public String getExtend2() {
		return extend2;
	}
	public void setExtend2(String extend2) {
		this.extend2 = extend2;
	}
	public String getExtend3() {
		return extend3;
	}
	public void setExtend3(String extend3) {
		this.extend3 = extend3;
	}
	@Override
	public String toString() {
		return "Customization [id=" + id + ", name=" + name + ", value=" + value + ", extend1=" + extend1 + ", extend2="
				+ extend2 + ", extend3=" + extend3 + "]";
	}
	public String getExtend4() {
		return extend4;
	}
	public void setExtend4(String extend4) {
		this.extend4 = extend4;
	}
	public String getExtend5() {
		return extend5;
	}
	public void setExtend5(String extend5) {
		this.extend5 = extend5;
	}
	
}
