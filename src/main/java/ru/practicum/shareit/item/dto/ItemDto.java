package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class ItemDto {
    Long id;
    @NotBlank(message = "Название не должно быть null или пустым")
    @Size(max = 255, message = "Название не должно превышать 255 символов")
    String name;
    @NotBlank(message = "Описание не должно быть null или пустым")
    @Size(max = 200, message = "Максимальная длина описания — 200 символов")
    String description;
    @NotNull(message = "Статус доступности не должно быть null или пустым")
    Boolean available;
    Long requestId;
}
