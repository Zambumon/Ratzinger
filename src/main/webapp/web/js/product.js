$('document').ready(function(){

    $('#volver').click(function(){

        //TODO: tal vez sexa millor substituir esto por un script que che devolva a ultima página visitada

        $("#action").val('index');

        $("#form").submit();
    });

});