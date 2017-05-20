package com.haipai.common.entity;

public class Employee {
	private int id;
	private String name;
	private int data1;
	private String imgUrl;
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the data1
	 */
	public int getData1() {
		return data1;
	}

	/**
	 * @param data1
	 *            the data1 to set
	 */
	public void setData1(int data1) {
		this.data1 = data1;
	}

}
