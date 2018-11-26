package viacheslav.pokhyliuk.projects.fxcolorimeter;

import javafx.beans.property.ReadOnlyObjectProperty;

public interface ColorsAnalyzer {
    void analyze();

    ReadOnlyObjectProperty<PointInfo> getInfo();
}
