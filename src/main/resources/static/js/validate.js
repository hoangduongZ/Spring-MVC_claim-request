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
        errorClass: "text-red-500 text-sm",
        // errorPlacement: function (error, element) {
        //     error.appendTo(element.parent());
        // }
    });
});
