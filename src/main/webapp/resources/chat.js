var yokla = function () {
    $.ajax({
        type: "GET",
        url: "/app/chat/" + $("#nick").val(),
        dataType: "text",
        success: function (mesaj) {
            $("ul").append("<li>" + mesaj + "</li>");
            yokla(); // Bir mesaj geldiginde tekrar yoklamaya basla.
        },
        error: function () {
            yokla(); // Bir hata oldugunda tekrar yoklamaya basla.
        }
    });
}


$("button").click(function () {

    if ($(".nick").css("visibility") === "visible") {
        $("textarea").prop("disabled", false);
        $(".nick").css("visibility", "hidden");
        $("span").html("Chat başladı..");

        // Yoklama islemi bir sefer baslatilmali
        yokla();
    }
    else
        $.ajax({
            type: "POST",
            url: "/app/chat/" + $("#nick").val(),
            dataType: "text",
            data: $("textarea").val(),
            contentType: "text/plain",
            success: function (data) {
                $("span").html(data);

                // Blink efekt
                $("span").fadeOut('fast', function () {
                    $(this).fadeIn('fast');
                });
            }
        });
});