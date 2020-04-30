<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../layout/taglib.jsp"%>
    
<script type="text/javascript">
    $(document).ready(function () {
        $('progress').hide();

        var _href = $("#oldUploadLink").attr("href");
        $('#oldUploadLink').attr("href", _href + "?CKEditor=" + getUrlParameter("CKEditor"));
        $('.trigger-file-input').click(function () {
            $('#file').click();
        });
        $("input:file").change(function () {

            $('progress').show();

            var files = document.getElementById('file').files;

            var formData = new FormData();
            formData.append("${_csrf.parameterName}", "${_csrf.token}");
            formData.append("CKEditor", getUrlParameter("CKEditor"));
            formData.append("CKEditorFuncNum", getUrlParameter("CKEditorFuncNum"));
            formData.append("langCode", getUrlParameter("langCode"));

            for (var i = 0; i < files.length; i++) {
                formData.append("file" + i, files[i]);

            }
            $.ajax({
                url: "/admin/home/upload-ajax",  //Server script to process data
                type: 'POST',
                xhr: function () {  // Custom XMLHttpRequest
                    var myXhr = $.ajaxSettings.xhr();
                    if (myXhr.upload) {
                        myXhr.upload.addEventListener('progress', progressHandlingFunction, false);
                    }
                    return myXhr;
                },
                //Ajax events
                beforeSend: beforeSendHandler,

                error: errorHandler,
                // Form data
                data: formData,
                //Options to tell jQuery not to process data or worry about content-type.
                cache: false,
                contentType: false,
                processData: false,
                success: function (e) {
                    $("#result").html(e);
                }

            });
        });

        function beforeSendHandler() {

        }

        function errorHandler(e) {
            alert("Bir hata meydana geldi. Lütfen tekrar deneyiniz");
        }

        function getUrlParameter(sParam) {
            var sPageURL = window.location.search.substring(1);
            var sURLVariables = sPageURL.split('&');
            for (var i = 0; i < sURLVariables.length; i++) {
                var sParameterName = sURLVariables[i].split('=');
                if (sParameterName[0] == sParam) {
                    return sParameterName[1];
                }
            }
        }

        function progressHandlingFunction(e) {
            if (e.lengthComputable) {
                $('progress').attr({value: e.loaded, max: e.total});
            }
        }
    });
</script>
<style type="text/css">
    ul li {
        list-style: none;
    }
</style>

<div class=".b-modern-upload">
	<span>Choose files to be uploaded
		If you encounter problems please <a id="oldUploadLink" href="<c:url value="/admin/home/uploadClassic"/>">click</a>
	</span>
	
	<ul>
		<li>
			<div>
				<button type="button" class="buttonSubmit trigger-file-input">DOSYA SEÇ</button>
			</div>
		</li>
		<li>
			<input type="file" id="file" multiple name="file[]">
		</li>
		<li>
			<progress></progress>
		</li>
	</ul>
	
	<div id="result"></div>
</div>