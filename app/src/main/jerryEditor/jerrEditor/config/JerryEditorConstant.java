package jerrEditor.config;

/**
 * author       : frog
 * time         : 2019-09-24 14:46
 * desc         : 多媒体常量
 * version      : 1.4.0
 */
public interface JerryEditorConstant {

    String AUDIO = "<audio src=\"{{audio}}\" controls=\"controls\" controlslist=\"nodownload\"></audio>";
    String VIDEO = "<video src=\"{{video}}\" controls=\"controls\" controlslist=\"nodownload\"></video>";
    String IMG = "<img src=\"{{img}}\"/>";

    String PARSER_AUDIO = "<audio src=\"\\{\\{audio\\}\\}\" controls=\"controls\" controlslist=\"nodownload\"></audio>";
    String PARSER_VIDEO = "<video src=\"\\{\\{video\\}\\}\" controls=\"controls\" controlslist=\"nodownload\"></video>";
    String PARSER_IMG = "<img src=\"\\{\\{img\\}\\}\"/>";
    String PARSER_AUDIO_2 = "<audio src='\\{\\{audio\\}\\}' controls controlsList='nodownload'></audio>";
    String PARSER_VIDEO_2 = "<video src='\\{\\{video\\}\\}' controls controlsList='nodownload'></video>";
}
