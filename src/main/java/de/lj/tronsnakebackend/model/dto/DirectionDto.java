package de.lj.tronsnakebackend.model.dto;

import de.lj.tronsnakebackend.service.game.player.DirectionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DirectionDto {
    private DirectionType direction;
}
