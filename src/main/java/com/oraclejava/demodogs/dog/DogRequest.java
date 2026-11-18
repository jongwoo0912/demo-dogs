package com.oraclejava.demodogs.dog;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

/** 등록/수정 요청 본문. id와 readcount는 서버가 관리하므로 받지 않는다. */
public record DogRequest(
		@NotBlank @Size(max = 100) String kind,
		@NotNull @PositiveOrZero Integer price,
		@Size(max = 255) String image,
		@Size(max = 100) String country,
		@Positive Double height,
		@Positive Double weight,
		String content) {
}
