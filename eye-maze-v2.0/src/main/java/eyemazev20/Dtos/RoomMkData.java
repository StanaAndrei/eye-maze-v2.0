package eyemazev20.Dtos;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class RoomMkData {
    private boolean isPublic;
    private boolean dim;

    public boolean isDim() {
        return dim;
    }

    public void setDim(boolean dim) {
        this.dim = dim;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }
}
