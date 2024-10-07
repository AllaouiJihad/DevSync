<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Add New User</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <script src="${pageContext.request.contextPath}/assets/js/UserValidator.js" type="text/javascript"></script>
    <link href="https://cdn.jsdelivr.net/npm/flowbite@2.5.2/dist/flowbite.min.css" rel="stylesheet" />
    <style>
        /* Ajout de styles personnalisés */
        .form-container {
            background-color: #f9fafb;
            border-radius: 10px;
            padding: 2rem;
            box-shadow: 0 10px 15px rgba(0, 0, 0, 0.1);
        }

        .form-container h1 {
            color: #1f2937;
        }

        .form-container input,
        .form-container select {
            transition: border-color 0.3s ease, box-shadow 0.3s ease;
        }

        .form-container input:focus,
        .form-container select:focus {
            border-color: #60a5fa;
            box-shadow: 0 0 0 3px rgba(96, 165, 250, 0.5);
        }

        .form-container button:hover {
            background-color: #2563eb;
        }

        .form-container button:focus {
            box-shadow: 0 0 0 3px rgba(37, 99, 235, 0.5);
        }
    </style>
</head>
<body>
<section class="bg-gray-50 dark:bg-gray-900 min-h-screen flex items-center justify-center">
    <div class="w-full max-w-lg">
        <div class="form-container">
            <h1 class="text-3xl font-semibold text-center mb-8">Add New User</h1>

            <form id="userForm" class="grid grid-cols-1 gap-6" action="users?action=create" method="post" onsubmit="return validateForm()">

                <div>
                    <label for="firstName" class="block text-sm font-medium text-gray-600 dark:text-gray-200">First Name</label>
                    <input type="text" name="firstName" id="firstName" placeholder="John" class="block w-full px-4 py-2 mt-2 bg-white border border-gray-300 rounded-md dark:bg-gray-900 dark:text-gray-300 dark:border-gray-700 focus:outline-none" />
                    <span id="firstNameError" class="text-red-500 text-sm"></span>
                </div>

                <div>
                    <label for="lastName" class="block text-sm font-medium text-gray-600 dark:text-gray-200">Last Name</label>
                    <input type="text" name="lastName" id="lastName" placeholder="Snow" class="block w-full px-4 py-2 mt-2 bg-white border border-gray-300 rounded-md dark:bg-gray-900 dark:text-gray-300 dark:border-gray-700 focus:outline-none" />
                    <span id="lastNameError" class="text-red-500 text-sm"></span>
                </div>

                <div>
                    <label for="email" class="block text-sm font-medium text-gray-600 dark:text-gray-200">Email Address</label>
                    <input type="email" name="email" id="email" placeholder="johnsnow@example.com" class="block w-full px-4 py-2 mt-2 bg-white border border-gray-300 rounded-md dark:bg-gray-900 dark:text-gray-300 dark:border-gray-700 focus:outline-none" />
                    <span id="emailError" class="text-red-500 text-sm"></span>
                </div>

                <div>
                    <label for="password" class="block text-sm font-medium text-gray-600 dark:text-gray-200">Password</label>
                    <input type="password" name="password" id="password" placeholder="Enter your password" class="block w-full px-4 py-2 mt-2 bg-white border border-gray-300 rounded-md dark:bg-gray-900 dark:text-gray-300 dark:border-gray-700 focus:outline-none" />
                    <span id="passwordError" class="text-red-500 text-sm"></span>
                </div>

                <div>
                    <label for="role" class="block text-sm font-medium text-gray-600 dark:text-gray-200">Role</label>
                    <select name="role" id="role" class="block w-full px-4 py-2 mt-2 bg-white border border-gray-300 rounded-md dark:bg-gray-900 dark:text-gray-300 dark:border-gray-700 focus:outline-none">
                        <option selected disabled>Choose a role</option>
                        <option value="MANAGER">Manager</option>
                        <option value="USER">User</option>
                    </select>
                    <span id="roleError" class="text-red-500 text-sm"></span>
                </div>

                <button type="submit" class="w-full py-3 mt-4 bg-blue-600 text-white rounded-md text-center font-semibold hover:bg-blue-500 focus:outline-none focus:ring focus:ring-blue-300">
                    Create User
                </button>
            </form>
        </div>
    </div>
</section>
<script src="https://cdn.jsdelivr.net/npm/flowbite@2.5.2/dist/flowbite.min.js"></script>
</body>
</html>
