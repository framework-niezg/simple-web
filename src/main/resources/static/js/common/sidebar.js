$(function () {
    $(".suojin").on("click", function () {
        if ($(".main").css("left") == "164px") {
            // $(".aside").css("display","block");
            $(".suojin").addClass("suojin2");
            $(".main").css("left", "30px");
            $(".main").css("min-width", "1336px");
        }
        else {
            // $(".aside").css("display","none");
            $(".suojin").removeClass("suojin2");
            $(".main").css("left", "164px");
            $(".main").css("min-width", "1200px");
        }
    });
})