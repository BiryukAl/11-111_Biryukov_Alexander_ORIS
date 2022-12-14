package com.example.semestr.repositories;

import com.example.semestr.entities.FileDC;
import com.example.semestr.entities.FilesAndNameHolderDC;
import com.example.semestr.exceptions.DbException;
import com.example.semestr.exceptions.DuplicateEntryException;
import com.example.semestr.exceptions.NoFoundRows;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class CRUDRepositoryFileImpl implements CRUDRepositoryFile {

    private final DataSource dataSource;

    public CRUDRepositoryFileImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private Function<ResultSet, FileDC> rsMapper = resultSet -> {
        try {
            Long idFile = resultSet.getLong("id");
            String title = resultSet.getString("title");
            String description = resultSet.getString("description");
            Long holderId = resultSet.getLong("holderId");
            String nameFile = resultSet.getString("nameFile");
            boolean publicAccess = resultSet.getBoolean("publicAccess");

            return FileDC.builder()
                    .id(idFile)
                    .title(title)
                    .description(description)
                    .holderId(holderId)
                    .nameFile(nameFile)
                    .publicAccess(publicAccess)
                    .build();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    };

    private Function<ResultSet, FilesAndNameHolderDC> fileAndNameHolderMapper = resultSet -> {
        try {
            Long idFile = resultSet.getLong("id");
            String title = resultSet.getString("title");
            String description = resultSet.getString("description");
            Long holderId = resultSet.getLong("holderId");
            String nameHolder = resultSet.getString("name");
            String nameFile = resultSet.getString("nameFile");
            boolean publicAccess = resultSet.getBoolean("publicAccess");

            return FilesAndNameHolderDC.builder()
                    .id(idFile)
                    .title(title)
                    .description(description)
                    .holderId(holderId)
                    .nameHolder(nameHolder)
                    .nameFile(nameFile)
                    .publicAccess(publicAccess)
                    .build();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    };


    //language=SQL
    private final String SQL_SAVE_FILE = "INSERT INTO file_oris(title, description, holderid, namefile, publicaccess) VALUES (?,?,?,?,?)";

    @Override
    public void save(FileDC file) throws DbException {
        try (PreparedStatement preparedStatement = dataSource.getConnection().prepareStatement(SQL_SAVE_FILE, PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            preparedStatement.setString(1, file.getTitle());
            preparedStatement.setString(2, file.getDescription());
            preparedStatement.setLong(3, file.getHolderId());
            preparedStatement.setString(4, file.getNameFile());
            preparedStatement.setBoolean(5, file.isPublicAccess());

            preparedStatement.executeUpdate();

            ResultSet keygen = preparedStatement.getGeneratedKeys();

            if (keygen.next()) {
                file.setId(keygen.getLong("id"));
            }

        } catch (SQLException e) {
            throw new DuplicateEntryException(e);
        }

    }

    @Override
    public List<FileDC> findAll() {
        return null;
    }

    //language=SQL
    private final String SQL_FIND_FILES_BY_PUBLIC_ACCESS = "SELECT * FROM file_oris WHERE publicaccess = ? ORDER BY id DESC ";

    public List<FileDC> findAllPublic() {
        List<FileDC> files = new ArrayList<>();

        try (PreparedStatement preparedStatement = dataSource.getConnection().prepareStatement(SQL_FIND_FILES_BY_PUBLIC_ACCESS);
        ) {
            preparedStatement.setBoolean(1, true);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                files.add(rsMapper.apply(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return files;
    }

    //language=SQL
    private final String SQL_FIND_FILES_BY_PUBLIC_ACCESS_LIMIT = "SELECT * FROM file_oris WHERE publicaccess  = ? ORDER BY id DESC LIMIT ? OFFSET ?  ";

    public List<FileDC> findAllPublic(int limit, int page) {
        List<FileDC> files = new ArrayList<>();

        try (PreparedStatement preparedStatement = dataSource.getConnection().prepareStatement(SQL_FIND_FILES_BY_PUBLIC_ACCESS_LIMIT);
        ) {
            preparedStatement.setBoolean(1, true);
            preparedStatement.setInt(2, limit);
            preparedStatement.setInt(3, page * limit);


            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                files.add(rsMapper.apply(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return files;
    }

    //language=SQL
    private final String SQL_FIND_FILES_BY_PUBLIC_NAME_USER_ACCESS_LIMIT = "SELECT f.id, f.title, f.description, f.holderid, u.name, f.namefile, f.publicaccess FROM file_oris f, user_oris_hm4 u  WHERE publicaccess  = ? AND f.holderid = u.id ORDER BY id DESC LIMIT ? OFFSET ? ";

    public List<FilesAndNameHolderDC> findAllPublicAndNameHolder(int limit, int page) {
        List<FilesAndNameHolderDC> files = new ArrayList<>();

        try (PreparedStatement preparedStatement = dataSource.getConnection()
                .prepareStatement(SQL_FIND_FILES_BY_PUBLIC_NAME_USER_ACCESS_LIMIT);
        ) {
            preparedStatement.setBoolean(1, true);
            preparedStatement.setInt(2, limit);
            preparedStatement.setInt(3, page * limit);


            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                files.add(fileAndNameHolderMapper.apply(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return files;
    }


    //language=SQL
    private final String SQL_FIND_FILES_BY_ID_USER = "SELECT * FROM file_oris WHERE holderid  = ? ORDER BY id DESC ";

    public List<FileDC> findByIdUser(Long idUser) {
        List<FileDC> files = new ArrayList<>();

        try (PreparedStatement preparedStatement = dataSource.getConnection().prepareStatement(SQL_FIND_FILES_BY_ID_USER);
        ) {
            preparedStatement.setLong(1, idUser);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                files.add(rsMapper.apply(resultSet));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return files;
    }


    //language=SQL
    private final String SQL_FIND_FILES_PUBLIC_BY_ID_USER = "SELECT * FROM file_oris WHERE holderid  = ? and publicaccess = true ORDER BY id DESC ";

    public List<FileDC> findPublicByIdUser(Long idUser) {
        List<FileDC> files = new ArrayList<>();

        try (PreparedStatement preparedStatement = dataSource.getConnection()
                .prepareStatement(SQL_FIND_FILES_PUBLIC_BY_ID_USER);
        ) {
            preparedStatement.setLong(1, idUser);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                files.add(rsMapper.apply(resultSet));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return files;
    }


    //language=SQL
    private final String SQL_FIND_FILES_BY_ID_USER_LIMIT = "SELECT * FROM file_oris WHERE holderid  = ? ORDER BY id DESC LIMIT ? OFFSET ?  ";

    public List<FileDC> findByIdUser(Long idUser, int limit, int page) {
        List<FileDC> files = new ArrayList<>();

        try (PreparedStatement preparedStatement = dataSource.getConnection().prepareStatement(SQL_FIND_FILES_BY_ID_USER_LIMIT);
        ) {
            preparedStatement.setLong(1, idUser);
            preparedStatement.setInt(2, limit);
            preparedStatement.setInt(3, page * limit);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                files.add(rsMapper.apply(resultSet));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return files;
    }


    //language=SQL
    private final String SQL_FIND_FILES_BY_ID = "SELECT * FROM file_oris WHERE id  = ? ORDER BY id DESC ";

    @Override
    public FileDC findById(Long id) {
        try (PreparedStatement preparedStatement = dataSource.getConnection().prepareStatement(SQL_FIND_FILES_BY_ID)
        ) {
            preparedStatement.setLong(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return rsMapper.apply(resultSet);
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //language=SQL
    private final String SQL_FIND_FILES_BY_TITLE = "SELECT * FROM file_oris WHERE title LIKE ? AND publicaccess = true ORDER BY id DESC";

    public List<FileDC> findByTitle(String title) {
        List<FileDC> files = new ArrayList<>();

        try (PreparedStatement preparedStatement = dataSource.getConnection().prepareStatement(SQL_FIND_FILES_BY_TITLE);
        ) {
            preparedStatement.setString(1, title + "%");

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                files.add(rsMapper.apply(resultSet));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return files;
    }


    //language=SQL
    private final String SQL_FIND_FILES_BY_TITLE_AND_DESCRIPTION = "SELECT * FROM file_oris WHERE publicaccess = true AND title LIKE ? AND description LIKE ? ORDER BY id DESC  ";

    public List<FileDC> findByTitleAndDescription(String title, String description) {
        List<FileDC> files = new ArrayList<>();

        try (PreparedStatement preparedStatement = dataSource.getConnection().prepareStatement(SQL_FIND_FILES_BY_TITLE_AND_DESCRIPTION);
        ) {
            preparedStatement.setString(1, title + "%");
            preparedStatement.setString(2, description + "%");

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                files.add(rsMapper.apply(resultSet));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return files;
    }


    //language=SQL
    private final String SQL_FIND_FILES_LINK_USER_BY_TITLE_AND_DESCRIPTION = "SELECT f.id, f.title, f.description, f.holderid, u.name, f.namefile, f.publicaccess FROM file_oris f, user_oris_hm4 u WHERE f.holderid = u.id AND  f.publicaccess = true AND LOWER(f.title) LIKE ? AND LOWER(f.description) LIKE ? ORDER BY f.id DESC ";

    public List<FilesAndNameHolderDC> findByNameHolderTitleAndDescription(String title, String description) {
        List<FilesAndNameHolderDC> files = new ArrayList<>();
        try (PreparedStatement preparedStatement = dataSource.getConnection()
                .prepareStatement(SQL_FIND_FILES_LINK_USER_BY_TITLE_AND_DESCRIPTION);
        ) {
            preparedStatement.setString(1, title + "%");
            preparedStatement.setString(2, description + "%");

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                files.add(fileAndNameHolderMapper.apply(resultSet));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return files;
    }


    //language=SQL
    private final String SQL_UPDATE = "UPDATE file_oris SET title = ? , description = ? , holderid = ? , namefile = ? , publicaccess = ? WHERE id = ? ";

    @Override
    public void update(FileDC file) {
        try (PreparedStatement preparedStatement = dataSource.getConnection().prepareStatement(SQL_UPDATE)) {
            preparedStatement.setString(1, file.getTitle());
            preparedStatement.setString(2, file.getDescription());
            preparedStatement.setLong(3, file.getHolderId());
            preparedStatement.setString(4, file.getNameFile());
            preparedStatement.setBoolean(5, file.isPublicAccess());
            preparedStatement.setLong(6, file.getId());

            int updRows = preparedStatement.executeUpdate();

            if (updRows == 0) {
                throw new NoFoundRows();
            }
        } catch (SQLException | NoFoundRows e) {
            throw new RuntimeException(e);
        }
    }

    //language=SQL
    private final String SQL_DELETE_BY_ID = "DELETE FROM file_oris WHERE id  = ? ";

    @Override
    public void delete(Long id) {
        try (PreparedStatement preparedStatement = dataSource.getConnection().prepareStatement(SQL_DELETE_BY_ID);
        ) {
            preparedStatement.setLong(1, id);
            int delRows = preparedStatement.executeUpdate();

            if (delRows == 0) {
                throw new NoFoundRows();
            }

        } catch (SQLException | NoFoundRows e) {
            throw new RuntimeException(e);
        }

    }


    //language=SQL
    private final String SQL_FILE_COUNT = "SELECT count(*) as count from file_oris";

    public Long countFiles() {
        try (Statement statement = dataSource.getConnection().createStatement();
             ResultSet resultSet = statement.executeQuery(SQL_FILE_COUNT);
        ) {

            if (resultSet.next()) {
                return resultSet.getLong("count");
            } else {
                return null;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
