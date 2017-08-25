package pw.ewen.WLPT.core.services;

import pw.ewen.WLPT.core.domains.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pw.ewen.WLPT.core.repositories.UserRepository;
import pw.ewen.WLPT.core.repositories.specifications.UserSpecificationBuilder;

import java.util.Collection;
import java.util.List;

/**
 * Created by wenliang on 17-4-14.
 */
@Service("userService")
public class UserService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //    /**
//     * 返回翻页格式用户列表
//     * @param spec 过滤表达式
//     * @param pr    分页对象
//     * @return
//     */
//    public Page<User> findAll(Specification<User> spec, PageRequest pr){
//        return this.userRepository.findAll(spec, pr);
//    }
//
//    /**
//     * 返回翻页格式用户列表
//     * @param pr    分页对象
//     * @return
//     */
//    public Page<User> findAll(PageRequest pr){
//        return this.userRepository.findAll(pr);
//    }

    public Collection<User> findAll(String filter){
        UserSpecificationBuilder builder = new UserSpecificationBuilder();
        if(filter.isEmpty()){
            return this.userRepository.findAll();
        } else {
            return this.userRepository.findAll(builder.build(filter));
        }
    }

    /**
     * 返回一个用户
     * @param id userId
     * @return 如果没有找到返回null
     */
    public User findOne(String id){
        return this.userRepository.findByuserId(id);
    }

    public User findOne(long id){
        return this.userRepository.findOne(id);
    }

    public User save(User user){
        return this.userRepository.save(user);
    }


    /**
     * 软删除
     * @param ids
     */
    public void delete(Collection<Long> ids){
        List<User> users = this.userRepository.findAll(ids);
        for(User user: users){
            user.setDeleted(true);
        }
        this.userRepository.save(users);
    }
}
