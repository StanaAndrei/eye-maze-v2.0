package eyemazev20.Dtos;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class StringDto {
    private String buffer;

    public String getBuffer() {
        return buffer;
    }

    public void setBuffer(String buffer) {
        this.buffer = buffer;
    }
}
