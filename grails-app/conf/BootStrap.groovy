import org.apache.commons.codec.digest.DigestUtils as DU

class BootStrap {

     def init = { servletContext ->
        println " *** Bootstrap Running ***"
        // add our data
        globalData()

        println "** create user test data **"
        createUserTestData()
     }

     def globalData = {
         // don't re run if we dont have to
         if (Keyword.list().size() == 0) {
             new Keyword(name: 'home', description: 'The home').save()
             new Keyword(name: 'java', description: 'The java').save()
             new Keyword(name: 'groovy', description: 'The groovy').save()
             new Keyword(name: 'ruby', description: 'The ruby').save()
         }
     }

     def createUserTestData = {

         // acegi items
          def userAuth = new Authority(authority:"ROLE_USER", description: "Authenticated User").save()
          def su = new Authority(authority:"ROLE_ADMIN", description: "Administrator Role").save()

          new Requestmap(url:"/**",configAttribute:"IS_AUTHENTICATED_ANONYMOUSLY").save()
          new Requestmap(url:"/todo/**",configAttribute:"IS_AUTHENTICATED_FULLY").save()
          new Requestmap(url:"/category/**",configAttribute:"IS_AUTHENTICATED_FULLY").save()
          new Requestmap(url:"/user/delete/**",configAttribute:"ROLE_ADMIN").save()
          new Requestmap(url:"/user/add/**",configAttribute:"IS_AUTHENTICATED_REMEMBERED").save()
          new Requestmap(url:"/user/list/**",configAttribute:"IS_AUTHENTICATED_ANONYMOUSLY").save()
          new Requestmap(url:"/user/edit/**",configAttribute:"IS_AUTHENTICATED_FULLY").save()


         // generic password
         def pass = DU.md5Hex("password")

         // Create a test user or 3
         def user1 = User.findByUserName("joseph")
         if (user1 == null) {
            user1 = new User(userName: 'joseph', password: pass, email: 'jnusairat@integrallis.com', firstName: 'Joseph', lastName: 'Nusairat').save()
         }
         def user2 = User.findByUserName("chris")
         if (user2 == null) {
            user2 = new User(userName: 'chris', password: pass, email: 'cjudd@juddsolutions.com', firstName: 'Chris', lastName: 'Judd').save()
         }
         def user3 = User.findByUserName("jim")
         if (user3 == null) {
            user3 = new User(userName: 'jim', password: pass, email: 'shinglerjim@gmail.com', firstName: 'Jim', lastName: 'Shingler').save()
         }

        // add to the authority items
         userAuth.addToPeople(user1)
         su.addToPeople(user2)
         userAuth.addToPeople(user3)

         userAuth.save()
         su.save()

         
         // create and save our buddy list
         def buddyList = BuddyList.findByName('Co-Workers')
         if (buddyList == null) {
            buddyList = new BuddyList(name: 'Co-Workers', description: '', owner: user1).save()
         }

         def buddyListMember = BuddyListMember.findByNickName('java judd')
         if (buddyListMember == null) {
             buddyListMember = new BuddyListMember(nickName: 'java judd', user: user2)
             buddyList.addToMembers(buddyListMember)
             buddyListMember.save()
         }

         // create a few categories
         def cat1 = Category.findByName('Work')
         if (cat1 == null) {
             cat1 = new Category(name: 'Work', description: 'All of our friends', user: user1).save()
         }
         if (Category.findByName('Family') == null) {
             new Category(name: 'Family', description: 'We are family', user: user1).save()
         }

         // a final TODO
//         def todo = Todo.findByName('Our First Task')
//         if (todo == null) {
//            todo = new Todo(owner: user1, category: cat1, name: 'Our First Task', createDate: new Date(), startDate: new Date(), priority: Priorities.Low, status: Statuses.Incomplete, dueDate: new Date() + 7, lastModifiedDate: new Date(), note: 'A note about our __task__.')
//             todo.save()
//         }
//
//         def todo2 = Todo.findByName('Our Second Task')
//         if (todo2 == null) {
//            todo2 = new Todo(owner: user1, category: cat1, name: 'Our Second Task', createDate: new Date(), startDate: new Date(), priority: Priorities.Medium, status: Statuses.Incomplete, dueDate: new Date() + 13, lastModifiedDate: new Date(), note: 'A note about our __second__ task.').save()
//         }
     }
     
     def destroy = {
     }
} 