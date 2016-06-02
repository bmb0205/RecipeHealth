module.exports = {

    options: {
        spawn: false,
        livereload: true
    },

    scripts: {
        files: [
            '../src/main/webapp/js/*.js'
        ],
        tasks: [
            'jshint',
            'uglify'
        ]
    },

    styles: {
        files: [
            '../src/main/webapp/css/*.css'
        ],
        tasks: [
            'cssmin'
        ]
    },
};