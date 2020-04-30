<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../layout/taglib.jsp"%>
    
<script type="text/javascript">
    $(document).ready(function () {
        var _href = $("#formUploadLink").attr("action");
        $('#formUploadLink').attr("action", _href + "?CKEditor=" + getUrlParameter("CKEditor"));
        $('.trigger-file-input').click(function () {
            $('input[type=file]').click();
        });
        $('.save-files').click(function () {
            var inp = document.getElementById('file');
            var fileNames = "Following files are uploading...";
            for (var i = 0; i < inp.files.length; ++i) {
                fileNames += inp.files.item(i).name + "";
            }
            $('#fileNames').html(fileNames);
        });
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
    });
</script>
<style type="text/css">
    ul li {
        list-style: none;
    }
</style>

<form method="POST" id="formSelectImage" action="${pageContext.request.contextPath}/file-upload/image-browse" 
	enctype="multipart/form-data">
	<span>Choose file or files to upload</span>
    <ul>
        <li>
            <div style="float: left;">
                <button type="button" class="buttonSubmit trigger-file-input">Choose File</button>
                <button type="submit" style="margin-left: 20px" class="buttonSubmit save-files">UPLOAD</button>
            </div>
            <div style="clear: both"></div>
        </li>
        <li>
            <span id="fileNames"></span>
        </li>
        <li>
            <input type="file" id="file" multiple name="file[]">
        </li>
 
    </ul>
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
</form>