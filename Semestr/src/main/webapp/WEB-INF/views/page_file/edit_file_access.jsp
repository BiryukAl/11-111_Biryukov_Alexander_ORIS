<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="/WEB-INF/views/_header.jsp" %>

<div class="page_layout _container">
    <h2 class="page_little_title">Edit</h2>
    <div class="page_container">
        <%@ include file="/WEB-INF/views/page_file/_page_files_side_bar_page.jsp" %>
        <div class="wrapper_selected_page">

            <div class="sing_flex">
                <form class="my_form" action="<c:url value="/file/edit/access?idFile=${idFile}"/>" method="post">
                    <p>Public access</p>
                    <input name="public_access" type="checkbox" <c:if test="${not empty public_access}">checked</c:if>>
                    <p>User access <b>(input id separated by a space)</b></p>
                    <input name="user_access" type="text" <c:if test="${not empty user_access}">value="<c:out value="${user_access}"/>"</c:if> >
                    <br>
                    <input type="submit" value="Edit">
                </form>
                <c:if test="${not empty message}">
                    <h4 class="for_server_msg">${message}</h4>
                </c:if>
            </div>
        </div>
    </div>
</div>
<%@include file="/WEB-INF/views/_footer.jsp" %>
