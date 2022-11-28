package com.example.semestr.servlets.subscribers;

import com.example.semestr.repositories.CRUDRepositoryFriendsImpl;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet("/subscriptions/subscribe")
public class SubscribeServlet extends HttpServlet {

    private CRUDRepositoryFriendsImpl repositoryFriends;

    @Override
    public void init() throws ServletException {
        repositoryFriends = (CRUDRepositoryFriendsImpl) getServletContext().getAttribute("repositoryFriends");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       Long idUser = (Long) request.getSession().getAttribute("user_id");
       Long idFriend = Long.valueOf(request.getParameter("idUser"));

       if (!idUser.equals(idFriend)){
           repositoryFriends.save(idUser, idFriend);
       }
        response.sendRedirect(getServletContext().getContextPath() + "/subscriptions");

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doGet(request, response);
    }
}
