package com.example.semestr.servlets.file;

import com.example.semestr.repositories.CRUDRepositoryFileImpl;
import com.example.semestr.services.DecorationPages;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/files/my")
public class MyFilesServlet extends HttpServlet {

    private CRUDRepositoryFileImpl repositoryFile;

    @Override
    public void init() throws ServletException {
        repositoryFile = (CRUDRepositoryFileImpl) getServletContext().getAttribute("repositoryFile");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DecorationPages.setTitle(request, "Files Disk");

        Long idUser = (Long) request.getSession().getAttribute("user_id");
        if (idUser != null) {
            // TODO: 30.11.2022 Сделать со страницами (Add LIMIT & OFFSET)
            request.setAttribute("items_my_files", repositoryFile.findByIdUser(idUser));
        }

        getServletContext().getRequestDispatcher("/WEB-INF/views/page_file/my_files.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doGet(request, response);
    }
}
