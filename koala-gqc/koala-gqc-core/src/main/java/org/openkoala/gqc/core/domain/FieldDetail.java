package org.openkoala.gqc.core.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.dayatang.domain.ValueObject;

/**
 * 字段明细，用于记录查询界面要显示的字段。
 * @author xmfang
 *
 */
@Embeddable
public class FieldDetail implements ValueObject, Comparable<FieldDetail> {

	private static final long serialVersionUID = -1104196839183330582L;

	/**
	 * 字段名称
	 */
	@Column(name = "FIELD_NAME")
	private String fieldName;
	
	/**
	 * 显示名称
	 */
	@Column(name = "LABEL")
	private String label;

	public FieldDetail() {
		
	}
	
	public FieldDetail(String fieldName) {
		this.fieldName = fieldName;
	}
	
	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public int compareTo(FieldDetail other) {
		return getFieldName().compareTo(other.getFieldName());
	}
	
}