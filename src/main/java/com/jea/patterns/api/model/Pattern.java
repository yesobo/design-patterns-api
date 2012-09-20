package com.jea.patterns.api.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * The persistent class for the patterns database table.
 * 
 */
@Entity
@Table(name="patterns")
@XmlRootElement
public class Pattern implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int id;

	@Lob
	private String applicability;

	private String category;

	@Lob
	private String intent;

	@Lob
	private String motivation;

	private String name;

	@Lob
	private String structure;

	public Pattern() {
		
	}
	
	public Pattern(int id, String name) {
		this.id = id;
		this.name = name;
		this.applicability = "";
		this.category = "";
		this.intent = "";
		this.motivation = "";
		this.structure = "";
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getApplicability() {
		return this.applicability;
	}

	public void setApplicability(String applicability) {
		this.applicability = applicability;
	}

	public String getCategory() {
		return this.category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getIntent() {
		return this.intent;
	}

	public void setIntent(String intent) {
		this.intent = intent;
	}

	public String getMotivation() {
		return this.motivation;
	}

	public void setMotivation(String motivation) {
		this.motivation = motivation;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStructure() {
		return this.structure;
	}

	public void setStructure(String structure) {
		this.structure = structure;
	}
}
