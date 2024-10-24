$(document).ready(function () {
    $("#employee-add").validate({
        rules: {
            firstname: {
                required: true,
                minlength: 2
            },
            lastname: {
                required: true,
                minlength: 2
            },
            gender: {
                required: true
            },
            dob: {
                required: true,
                date: true
            },
            address: {
                required: true,
                minlength: 5
            },
            departmentId: {
                required: true
            },
            'accountRegisterDTO.userName': {
                required: true,
                minlength: 3
            },
            'accountRegisterDTO.email': {
                required: true,
                email: true
            },
            'accountRegisterDTO.password': {
                required: true,
                minlength: 6
            },
            accountRole: {
                required: true
            }
        },
        messages: {
            firstname: {
                required: "First Name is required",
                minlength: "First Name must be at least 2 characters"
            },
            lastname: {
                required: "Last Name is required",
                minlength: "Last Name must be at least 2 characters"
            },
            gender: {
                required: "Gender is required"
            },
            dob: {
                required: "Date of Birth is required",
                date: "Please enter a valid date"
            },
            address: {
                required: "Address is required",
                minlength: "Address must be at least 5 characters"
            },
            departmentId: {
                required: "Please select a department"
            },
            'accountRegisterDTO.userName': {
                required: "Username is required",
                minlength: "Username must be at least 3 characters"
            },
            'accountRegisterDTO.email': {
                required: "Email is required",
                email: "Please enter a valid email address"
            },
            'accountRegisterDTO.password': {
                required: "Password is required",
                minlength: "Password must be at least 6 characters"
            },
            accountRole: {
                required: "Please select a role"
            }
        },
        // errorClass: "text-red-500 text-sm",
        // errorPlacement: function (error, element) {
        //     error.appendTo(element.parent());
        // }
    });
    $("#employee-update").validate({
        rules: {
            firstname: {
                required: true,
                minlength: 2
            },
            lastname: {
                required: true,
                minlength: 2
            },
            gender: {
                required: true
            },
            dob: {
                required: true,
                date: true
            },
            address: {
                required: true,
                minlength: 5
            },
            departmentId: {
                required: true
            },
            'accountRegisterDTO.userName': {
                required: true,
                minlength: 3
            },
            'accountRegisterDTO.email': {
                required: true,
                email: true
            },
            'accountRegisterDTO.password': {
                required: true,
                minlength: 6
            },
            accountRole: {
                required: true
            }
        },
        messages: {
            firstname: {
                required: "First Name is required",
                minlength: "First Name must be at least 2 characters"
            },
            lastname: {
                required: "Last Name is required",
                minlength: "Last Name must be at least 2 characters"
            },
            gender: {
                required: "Gender is required"
            },
            dob: {
                required: "Date of Birth is required",
                date: "Please enter a valid date"
            },
            address: {
                required: "Address is required",
                minlength: "Address must be at least 5 characters"
            },
            departmentId: {
                required: "Please select a department"
            },
            'accountRegisterDTO.userName': {
                required: "Username is required",
                minlength: "Username must be at least 3 characters"
            },
            'accountRegisterDTO.email': {
                required: "Email is required",
                email: "Please enter a valid email address"
            },
            'accountRegisterDTO.password': {
                required: "Password is required",
                minlength: "Password must be at least 6 characters"
            },
            accountRole: {
                required: "Please select a role"
            }
        },
        // errorClass: "text-red-500 text-sm",
        // errorPlacement: function (error, element) {
        //     error.appendTo(element.parent());
        // }
    });
    $("#project-add").validate({
        rules: {
            name: {
                required: true,
                minlength: 3
            },
            description: {
                required: true,
                minlength: 5
            },
            startDate: {
                required: true,
                date: true
            },
            endDate: {
                required: true,
                date: true,
                greaterThanStartDate: true // Custom rule
            },
            budget: {
                required: true,
                number: true,
                min: 0
            },
            status: {
                required: true
            }
        },
        messages: {
            name: {
                required: "Project name is required",
                minlength: "Project name must be at least 3 characters long"
            },
            description: {
                required: "Description is required",
                minlength: "Description must be at least 5 characters long"
            },
            startDate: {
                required: "Start date is required",
                date: "Please enter a valid date"
            },
            endDate: {
                required: "End date is required",
                date: "Please enter a valid date",
                greaterThanStartDate: "End date must be after start date"
            },
            budget: {
                required: "Budget is required",
                number: "Please enter a valid number",
                min: "Budget cannot be negative"
            },
            status: {
                required: "Please select a project status"
            }
        }
    });

    $.validator.addMethod("greaterThanStartDate", function (value, element) {
        var startDate = $('#startDate').val();
        return Date.parse(value) > Date.parse(startDate);
    }, "End date must be after start date.");

    $("#project-update").validate({
        rules: {
            name: {
                required: true,
                minlength: 3
            },
            description: {
                required: true,
                minlength: 5
            },
            startDate: {
                required: true,
                date: true
            },
            endDate: {
                required: true,
                date: true,
                greaterThanStartDate: true // Custom rule
            },
            budget: {
                required: true,
                number: true,
                min: 0
            },
            status: {
                required: true
            }
        },
        messages: {
            name: {
                required: "Project name is required",
                minlength: "Project name must be at least 3 characters long"
            },
            description: {
                required: "Description is required",
                minlength: "Description must be at least 5 characters long"
            },
            startDate: {
                required: "Start date is required",
                date: "Please enter a valid date"
            },
            endDate: {
                required: "End date is required",
                date: "Please enter a valid date",
                greaterThanStartDate: "End date must be after start date"
            },
            budget: {
                required: "Budget is required",
                number: "Please enter a valid number",
                min: "Budget cannot be negative"
            },
            status: {
                required: "Please select a project status"
            }
        }
    });
});
