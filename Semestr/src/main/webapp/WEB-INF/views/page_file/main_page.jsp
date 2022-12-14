<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="/WEB-INF/views/_header.jsp" %>
<div class="page_layout _container">
    <h2 class="page_little_title">Public Files</h2>
    <div class="page_container">
        <%@ include file="/WEB-INF/views/page_file/_page_files_side_bar_page.jsp" %>
        <div class="wrapper_selected_page">
            <h3 class="selected_page_h3">Total files on disk: ${count_file}</h3>
            <%@ include file="/WEB-INF/views/page_file/_forEach_public_files.jsp" %>
            <c:if test="${count_file > 10}">
                <a class="btn_item_file" href="<c:url value="/main?page=1"/>">Next page</a>
            </c:if>
        </div>
    </div>
</div>
<%@include file="/WEB-INF/views/_footer.jsp" %>
