/**
 * 基础js
 */
$(function () {

});
function changemainframe(framename,putvalue) {
    $("#mainpanelpage").attr("src",framename+"?"+putvalue);
}