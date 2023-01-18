package cn.saladday.rjTakeOut.controller;

import cn.saladday.rjTakeOut.common.CustomException;
import cn.saladday.rjTakeOut.common.R;
import cn.saladday.rjTakeOut.domain.AddressBook;
import cn.saladday.rjTakeOut.mapper.AddressBookMapper;
import cn.saladday.rjTakeOut.service.AddressBookService;
import cn.saladday.rjTakeOut.utils.ThreadLocalEmpIdDataUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.apache.tomcat.jni.Address;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/addressBook")
public class AddressBookController {

    @Autowired
    private AddressBookService addressBookService;

    @PostMapping
    public R<String> save(@RequestBody AddressBook addressBook){
        Long userId = ThreadLocalEmpIdDataUtil.getUserId();
        addressBook.setUserId(userId);
        addressBookService.save(addressBook);
        return R.success("");
    }

    @GetMapping("/default")
    public R<AddressBook> getDefault(){
        LambdaQueryWrapper<AddressBook> lqw = new LambdaQueryWrapper<AddressBook>();
        lqw.eq(AddressBook::getIsDefault,1);
        AddressBook one = addressBookService.getOne(lqw);
        if(one==null)throw new CustomException("没有默认地址，请添加");
        return R.success(one);
    }

    @GetMapping("/list")
    public R<List<AddressBook>> findList(){
        Long userId = ThreadLocalEmpIdDataUtil.getUserId();
        LambdaQueryWrapper<AddressBook> lqw = new LambdaQueryWrapper<AddressBook>();
        lqw.eq(AddressBook::getUserId,userId);
        List<AddressBook> list = addressBookService.list(lqw);
        return R.success(list);
    }

    @PutMapping("/default")
    public R<AddressBook> updateDefault(@RequestBody AddressBook addressBook){

        //其余的都要设置为0
        Long id = addressBook.getId();
        LambdaUpdateWrapper<AddressBook> luw2 = new LambdaUpdateWrapper<AddressBook>();
        luw2.set(AddressBook::getIsDefault,0);
        addressBookService.update(luw2);

        //其设为1
        addressBook.setIsDefault(1);
        addressBookService.updateById(addressBook);

        return R.success(addressBook);
    }
}
