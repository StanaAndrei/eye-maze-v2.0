package eyemazev20.Dtos.http;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GlobalCustomMzDto {
    private int id;
    private String name, creator;
}
