<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Gestion des Tags</title>
  <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
</head>
<body>
<div class="container mt-5">
  <h2>Gestion des Tags</h2>

  <form action="tags?action=add" method="post">
    <div class="form-group">
      <label for="name">Nom du Tag:</label>
      <input type="text" class="form-control" id="name" name="name" required>
    </div>
    <button type="submit" class="btn btn-primary">Ajouter Tag</button>
  </form>

  <h3 class="mt-4">Liste des Tags</h3>
  <table class="table">
    <thead>
    <tr>
      <th>ID</th>
      <th>Nom</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="tag" items="${tags}">
      <tr>
        <td>${tag.id}</td>
        <td>${tag.name}</td>

      </tr>
    </c:forEach>
    </tbody>
  </table>
</div>
</body>
</html>