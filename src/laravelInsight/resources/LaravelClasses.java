package net.rentalhost.idea.laravelInsight.resources;

public enum LaravelClasses {
    ELOQUENT_MODEL("\\Illuminate\\Database\\Eloquent\\Model"),
    ELOQUENT_SOFTDELETES_TRAIT("\\Illuminate\\Database\\Eloquent\\SoftDeletes");

    private final String text;

    LaravelClasses(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
