package pointclub.shared.service;

import androidx.constraintlayout.widget.ConstraintSet;

public class ConstraintsService {

    public static void addCenterVerticallyConstraint(ConstraintSet constraintSet, int childViewId, int parentViewId) {
        constraintSet.connect(childViewId, ConstraintSet.TOP, parentViewId, ConstraintSet.TOP);
        constraintSet.connect(childViewId, ConstraintSet.BOTTOM, parentViewId, ConstraintSet.BOTTOM);
    }
}