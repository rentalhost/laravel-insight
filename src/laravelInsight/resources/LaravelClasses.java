package net.rentalhost.idea.laravelInsight.resources;

public enum LaravelClasses {
    ELOQUENT_MODEL("\\Illuminate\\Database\\Eloquent\\Model"),
    ELOQUENT_SOFTDELETES_TRAIT("\\Illuminate\\Database\\Eloquent\\SoftDeletes"),
    ELOQUENT_RELATIONSHIP_HASONE("\\Illuminate\\Database\\Eloquent\\Relations\\HasOne"),
    ELOQUENT_RELATIONSHIP_HASMANY("\\Illuminate\\Database\\Eloquent\\Relations\\HasMany"),
    ELOQUENT_RELATIONSHIP_HASMANYTHROUGHT("\\Illuminate\\Database\\Eloquent\\Relations\\HasManyThrough"),
    ELOQUENT_RELATIONSHIP_MORPHTO("\\Illuminate\\Database\\Eloquent\\Relations\\MorphTo"),
    ELOQUENT_RELATIONSHIP_MORPHONE("\\Illuminate\\Database\\Eloquent\\Relations\\MorphOne"),
    ELOQUENT_RELATIONSHIP_MORPHMANY("\\Illuminate\\Database\\Eloquent\\Relations\\MorphMany"),
    ELOQUENT_RELATIONSHIP_MORPHTOMANY("\\Illuminate\\Database\\Eloquent\\Relations\\MorphToMany"),
    ELOQUENT_RELATIONSHIP_BELONGSTO("\\Illuminate\\Database\\Eloquent\\Relations\\BelongsTo"),
    ELOQUENT_RELATIONSHIP_BELONGSTOMANY("\\Illuminate\\Database\\Eloquent\\Relations\\BelongsToMany"),
    ELOQUENT_COLLECTION("\\Illuminate\\Support\\Collection");

    private final String classname;

    LaravelClasses(final String classname) {
        this.classname = classname;
    }

    @Override
    public String toString() {
        return classname;
    }
}
