package net.rentalhost.idea.laravelInsight.resources;

public enum CarbonClasses {
    CARBON("\\Carbon\\Carbon");

    private final String classname;

    CarbonClasses(final String classname) {
        this.classname = classname;
    }

    @Override
    public String toString() {
        return classname;
    }
}
