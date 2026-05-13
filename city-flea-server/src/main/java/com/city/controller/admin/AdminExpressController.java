package com.city.controller.admin;

import com.city.dto.ExpressCompanyDTO;
import com.city.entity.ExpressCompany;
import com.city.result.Result;
import com.city.service.ExpressCompanyService;
import com.city.vo.ExpressCompanyVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/admin/express")
@Tag(name = "快递公司管理")
@Slf4j
public class AdminExpressController {

    @Autowired
    private ExpressCompanyService expressCompanyService;

    @GetMapping("/list")
    @Operation(summary = "快递公司列表")
    public Result<List<ExpressCompanyVO>> list() {
        return Result.success(expressCompanyService.listAll());
    }

    @GetMapping("/enabled")
    @Operation(summary = "启用的快递公司列表")
    public Result<List<ExpressCompanyVO>> listEnabled() {
        return Result.success(expressCompanyService.listEnabled());
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID获取快递公司")
    public Result<ExpressCompanyVO> getById(@PathVariable Long id) {
        ExpressCompanyVO vo = expressCompanyService.getById(id);
        return Result.success(vo);
    }

    @PostMapping
    @Operation(summary = "新增快递公司")
    public Result<String> add(@RequestBody ExpressCompanyDTO dto) {
        expressCompanyService.add(dto);
        return Result.success("新增成功");
    }

    @PutMapping
    @Operation(summary = "修改快递公司")
    public Result<String> update(@RequestBody ExpressCompanyDTO dto) {
        expressCompanyService.update(dto);
        return Result.success("修改成功");
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除快递公司")
    public Result<String> delete(@PathVariable Long id) {
        expressCompanyService.deleteById(id);
        return Result.success("删除成功");
    }

    @PutMapping("/enabled/{id}")
    @Operation(summary = "启用/禁用快递公司")
    public Result<String> toggleEnabled(@PathVariable Long id, @RequestParam Integer enabled) {
        expressCompanyService.toggleEnabled(id, enabled);
        return Result.success(enabled == 1 ? "启用成功" : "禁用成功");
    }

    @PutMapping("/default/{id}")
    @Operation(summary = "设为默认快递公司")
    public Result<String> setDefault(@PathVariable Long id) {
        expressCompanyService.setDefault(id);
        return Result.success("设置成功");
    }
}
