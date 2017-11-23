/**
 * Created by longbridge on 11/23/17.
 */
function viewCard(image) {
    console.log("ok");
    $("#viewCardModal").modal('show');
    $('#viewCardModal').on('shown.bs.modal', function () {
        $('#myInput').html("<img src='"+image+"' />")
    });
}