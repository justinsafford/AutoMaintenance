environments {

    dev {
        cf {
            target = 'https://api.run.pivotal.io'
            space = 'development'
            uri = 'http://automaintenance-dev.cfapps.io'
            application = 'AutoMaintenance-dev'
        }
    }

    staging {
        cf {
            target = 'https://api.run.pivotal.io'
            space = 'staging'
            uri = 'http://automaintenance-staging.cfapps.io'
            application = 'AutoMaintenance-staging'
        }
    }
}