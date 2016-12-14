package at.shop.presentation;

import at.shop.domain.CurrentUser;
import at.shop.facade.ProductFacade;
import at.shop.facade.commands.product.CreateProductCommand;
import at.shop.facade.commands.product.DeleteProductCommand;
import at.shop.facade.views.ProductView;
import at.shop.validation.ProductValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Controller
@ControllerAdvice
@RequestMapping(value = "/product")
public class ProductController {

    private final ProductValidator productValidator;
    private final ProductFacade productFacade;

    @InitBinder("product")
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(productValidator);
    }

    //with controlleradvice and this method every view can have access to the current user via "currentUser"
    @ModelAttribute("currentUser")
    public CurrentUser getCurrentUser(Authentication authentication) {
        return (authentication == null) ? null : (CurrentUser) authentication.getPrincipal();
    }


    //Showing products/data sets
    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public String showAllProducts(Model model) {

        List<ProductView> productViews = productFacade.getProducts();
        model.addAttribute("products", productViews);
        return "product/showAllProducts";
    }

    @PreAuthorize("@currentUserService.hasAccessForViewProducts(principal)")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ModelAndView showProductWithPath(@PathVariable Long id, ModelAndView mv, BindingResult bindingResult) {
        return _showProduct(id, mv, bindingResult);
    }

    //Adding products/data
    @PreAuthorize("@currentUserService.hasAccessForProducts(principal)")
    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public ModelAndView addProductWithForm(ModelAndView mv) {
        mv.setViewName("product/createForm");
        mv.getModelMap().addAttribute("product", CreateProductCommand.of("Enter name",
                "Enter producer", "Description", 10d, 10, "default.png"));
        return mv;
    }

    @PreAuthorize("@currentUserService.hasAccessForProducts(principal)")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ModelAndView handleAddProductWithForm(@Valid @ModelAttribute("product") CreateProductCommand command,
                                                 BindingResult bindingResult, ModelAndView mv) {
        if (bindingResult.hasErrors()) {
            mv.setViewName("product/createForm");
            return mv;
        }
        ProductView productView = productFacade.createProduct(command);
        if (productView.getId() > 0) {
            mv.setViewName("redirect:/product/all");
        } else {
            mv.setViewName("product/createForm");
        }
        return mv;
    }

    //Details & download
    @PreAuthorize("@currentUserService.hasAccessForViewProducts(principal)")
    @RequestMapping(value = "/detail/{id}", method = RequestMethod.GET)
    public ModelAndView detailProduct(@PathVariable Long id, ModelAndView mv) {
        ProductView productView = productFacade.getProduct(id);
        mv.setViewName("/product/detail");
        CreateProductCommand command = CreateProductCommand
                .of(productView.getName(), productView.getProducer(), productView.getDescription(),
                        productView.getPrice(), productView.getStock(), productView.getUrl());
        mv.getModelMap().addAttribute("product", command);
        return mv;
    }

    //Edit products/data
    @PreAuthorize("@currentUserService.hasAccessForProducts(principal)")
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public ModelAndView showEditProduct(@PathVariable Long id, ModelAndView mv) {
        ProductView productView = productFacade.getProduct(id);
        mv.setViewName("/product/editForm");
        mv.getModelMap().addAttribute("product", CreateProductCommand.of(productView.getName()
                , productView.getProducer(), productView.getDescription(), productView.getPrice(),
                productView.getStock(),
                productView.getUrl()));
        return mv;
    }

    @PreAuthorize("@currentUserService.hasAccessForProducts(principal)")
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.POST)
    public ModelAndView handleEditProduct(
            @PathVariable Long id, @Valid @ModelAttribute("product") CreateProductCommand command,
            BindingResult bindingResult, ModelAndView mv) {
        ProductView productView = productFacade.editProduct(id, command);
        if (productView.getId() > 0) {
            mv.setViewName("redirect:/product/all");
        } else {
            mv.setViewName("product/editForm");
        }
        return mv;
    }

    //Delete products/data
    @PreAuthorize("@currentUserService.hasAccessForProducts(principal)")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public ModelAndView deleteProduct(@PathVariable Long id, ModelAndView mv) {
        productFacade.deleteProduct(DeleteProductCommand.of(id));
        mv.setViewName("redirect:/product/all");
        return mv;
    }

    private ModelAndView _showProduct(@NotNull Long id, ModelAndView mv, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            mv.setViewName("errorView");
            return mv;
        }
        ProductView productView = productFacade.getProduct(id);
        mv.getModelMap().addAttribute("products", Arrays.asList(productView));
        mv.setViewName("product/showAllProducts");
        return mv;
    }

}
