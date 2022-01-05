package com.annpurna.cli.common.model;

import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

@DataType()
public class AnnpurnaWallet {

	@Property()
	private String id ;
	@Property()
	private long createdOn ;
	@Property()
	private String createdBy ;
	@Property()
	private String owner ;
	@Property()
	private String status ;
	@Property()
	private Object properties ;
	@Property()
	private Long value;
	@Property
	private String secret ;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public long getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(long createdOn) {
		this.createdOn = createdOn;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Object getProperties() {
		return properties;
	}

	public void setProperties(Object properties) {
		this.properties = properties;
	}

	public Long getValue() {
		return this.value ;
	}
	
	public void setValue(Long value) {
		this.value = value;
	}
	
	
	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	@Override
	public String toString() {
		return "Wallet [id=" + id + ", createdOn=" + createdOn + ", createdBy=" + createdBy + ","
				+ ", status=" + status + ", properties=" + properties + ",secret="+secret+",owner="+owner+"]";
	}
	
}