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
            },
            errorClass: "text-red-600",
        },
        highlight: function(element) {
            $(element).addClass("border-red-500");
        },
        unhighlight: function(element) {
            $(element).removeClass("border-red-500");
        },
        errorPlacement: function(error, element) {
            if (element.attr("name") == "gender") {
                error.appendTo(".error-gender");
            } else {
                error.insertAfter(element);
            }
        },
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
            },
            errorClass: "text-red-600",
        },
        highlight: function(element) {
            $(element).addClass("border-red-500");
        },
        unhighlight: function(element) {
            $(element).removeClass("border-red-500");
        },
        errorPlacement: function(error, element) {
            if (element.attr("name") == "gender") {
                error.appendTo(".error-gender");
            } else {
                error.insertAfter(element);
            }
        },
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
            },
            errorClass: "text-red-600",
        },
        highlight: function(element) {
            $(element).addClass("border-red-500");
        },
        unhighlight: function(element) {
            $(element).removeClass("border-red-500");
        }
    });

    $.validator.addMethod("greaterThanStartDate", function (value, element) {
        var startDate = $('#startDate').val();
        return value > startDate;
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
                greaterThanStartDate: true
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
            },
            errorClass: "text-red-600",

        },
        highlight: function(element) {
            $(element).addClass("border-red-500");
        },
        unhighlight: function(element) {
            $(element).removeClass("border-red-500");
        }
    });

    $("#claim-add").validate({
        rules: {
            "title": {
                required: true,
                maxlength: 100
            },
            "requestReason": {
                required: true,
                maxlength: 500
            },
            "claimDetails[0].startTime": {
                required: true,
                date: true
            },
            "claimDetails[0].endTime": {
                required: true,
                date: true
            },
        },
        messages: {
            "title": {
                required: "Please enter the claim title.",
                maxlength: "The title must be less than 100 characters."
            },
            "requestReason": {
                required: "Please provide a reason for the request.",
                maxlength: "The reason must be less than 500 characters."
            },
            "claimDetails[0].startTime": {
                required: "Please select a start time.",
                date: "Please enter a valid date."
            },
            "claimDetails[0].endTime": {
                required: "Please select an end time.",
                date: "Please enter a valid date."
            }
        },
        errorClass: "text-red-600",
        highlight: function(element) {
            $(element).addClass("border-red-500");
        },
        unhighlight: function(element) {
            $(element).removeClass("border-red-500");
        }
    });

    $("#claim-update").validate({
        rules: {
            "title": {
                required: true,
                maxlength: 100
            },
            "requestReason": {
                required: true,
                maxlength: 500
            },
            "claimDetails[0].startTime": {
                required: true,
                date: true
            },
            "claimDetails[0].endTime": {
                required: true,
                date: true
            },
        },
        messages: {
            "title": {
                required: "Please enter the claim title.",
                maxlength: "The title must be less than 100 characters."
            },
            "requestReason": {
                required: "Please provide a reason for the request.",
                maxlength: "The reason must be less than 500 characters."
            },
            "claimDetails[0].startTime": {
                required: "Please select a start time.",
                date: "Please enter a valid date."
            },
            "claimDetails[0].endTime": {
                required: "Please select an end time.",
                date: "Please enter a valid date."
            }
        },
        errorClass: "text-red-600",
        highlight: function(element) {
            $(element).addClass("border-red-500");
        },
        unhighlight: function(element) {
            $(element).removeClass("border-red-500");
        }
    });

    $("#password-change").validate({
        rules: {
            currentPassword: {
                required: true
            },
            newPassword: {
                required: true,
                minlength: 6
            },
            confirmPassword: {
                required: true,
                equalTo: "#newPassword"
            }
        },
        messages: {
            currentPassword: {
                required: "Please enter your current password."
            },
            newPassword: {
                required: "Please enter a new password.",
                minlength: "Your password must be at least 6 characters long."
            },
            confirmPassword: {
                required: "Please confirm your new password.",
                equalTo: "New password and confirmation password do not match."
            }
        },
        submitHandler: function (form) {
            form.submit();
        },
        highlight: function(element) {
            $(element).addClass("border-red-500");
        },
        unhighlight: function(element) {
            $(element).removeClass("border-red-500");
        }
    });

    $("#resetPasswordForm").validate({
        rules: {
            newPassword: {
                required: true,
                minlength: 6
            },
            confirmPassword: {
                required: true,
                equalTo: "#newPassword"
            }
        },
        messages: {
            newPassword: {
                required: "Please enter a new password.",
                minlength: "Password must be at least 6 characters long."
            },
            confirmPassword: {
                required: "Please confirm your password.",
                equalTo: "Passwords do not match."
            }
        },
        errorClass: "text-red-500 text-sm mt-1",
        validClass: "text-green-500 text-sm mt-1",
        submitHandler: function (form) {
            form.submit();
        },
        highlight: function(element) {
            $(element).addClass("border-red-500");
        },
        unhighlight: function(element) {
            $(element).removeClass("border-red-500");
        }
    });

});
