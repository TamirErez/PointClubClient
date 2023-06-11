package pointclub.shared.service;

import androidx.constraintlayout.widget.ConstraintSet;

public class ConstraintsService {

    public static void addCenterVerticallyConstraint(ConstraintSet constraintSet, int childViewId, int parentViewId) {
        constraintSet.connect(childViewId, ConstraintSet.TOP, parentViewId, ConstraintSet.TOP);
        constraintSet.connect(childViewId, ConstraintSet.BOTTOM, parentViewId, ConstraintSet.BOTTOM);
    }

    public static void addCenterHorizontallyConstraint(ConstraintSet constraintSet, int childViewId, int parentViewId) {
        constraintSet.connect(childViewId, ConstraintSet.START , parentViewId, ConstraintSet.START);
        constraintSet.connect(childViewId, ConstraintSet.END, parentViewId, ConstraintSet.END);
    }

    public static void addTopToBottomConstraint(ConstraintSet constraintSet, int childViewId, int parentViewId) {
        constraintSet.connect(childViewId, ConstraintSet.TOP , parentViewId, ConstraintSet.BOTTOM);
    }

    public static void addStartToStartConstraint(ConstraintSet constraintSet, int childViewId, int parentViewId) {
        constraintSet.connect(childViewId, ConstraintSet.START , parentViewId, ConstraintSet.START);
    }

    public static void addTopToTopConstraint(ConstraintSet constraintSet, int childViewId, int parentViewId) {
        constraintSet.connect(childViewId, ConstraintSet.TOP , parentViewId, ConstraintSet.TOP);
    }
}