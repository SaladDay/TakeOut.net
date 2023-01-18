package cn.saladday.rjTakeOut.service.impl;

import cn.saladday.rjTakeOut.domain.AddressBook;
import cn.saladday.rjTakeOut.mapper.AddressBookMapper;
import cn.saladday.rjTakeOut.service.AddressBookService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {
}
