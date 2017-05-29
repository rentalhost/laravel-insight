package net.rentalhost.idea.laravelInsight.resources;

public enum LaravelClasses {
    ELOQUENT_MODEL("\\Illuminate\\Database\\Eloquent\\Model");

    private final String text;

    LaravelClasses(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
