// don't upgrade (for compatibility with LightCompressor)
object Mp4Parser : DependencyGroup("com.googlecode.mp4parser", "1.0.6") {

    object IsoParser : Dependency(Mp4Parser.group, "isoparser", Mp4Parser.version)

}