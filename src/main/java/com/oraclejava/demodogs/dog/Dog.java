package com.oraclejava.demodogs.dog;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "dogs")
public class Dog {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/** 개 품종 */
	@Column(nullable = false, length = 100)
	private String kind;

	/** 가격 (원) */
	@Column(nullable = false)
	private Integer price;

	/** 이미지 URL 또는 파일명 */
	private String image;

	/** 원산지 */
	@Column(length = 100)
	private String country;

	/** 키 (cm) */
	private Double height;

	/** 몸무게 (kg) */
	private Double weight;

	/** 개 설명 (MySQL TEXT) */
	@Column(columnDefinition = "TEXT")
	private String content;

	/** 조회수 */
	@Column(nullable = false)
	private int readcount;

	protected Dog() {
	}

	public Dog(DogRequest request) {
		update(request);
	}

	public void update(DogRequest request) {
		this.kind = request.kind();
		this.price = request.price();
		this.image = request.image();
		this.country = request.country();
		this.height = request.height();
		this.weight = request.weight();
		this.content = request.content();
	}

	public void increaseReadcount() {
		this.readcount++;
	}

	public Long getId() { return id; }
	public String getKind() { return kind; }
	public Integer getPrice() { return price; }
	public String getImage() { return image; }
	public String getCountry() { return country; }
	public Double getHeight() { return height; }
	public Double getWeight() { return weight; }
	public String getContent() { return content; }
	public int getReadcount() { return readcount; }

}
