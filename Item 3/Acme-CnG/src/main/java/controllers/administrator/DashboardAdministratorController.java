//
// package controllers.administrator;
//
// import java.util.ArrayList;
// import java.util.Collection;
//
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Controller;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestMethod;
// import org.springframework.web.servlet.ModelAndView;
//
// import services.ApplicationService;
// import services.CommentService;
// import services.CustomerService;
// import services.MessageService;
// import services.OfferService;
// import services.RequestService;
// import services.TripService;
// import controllers.AbstractController;
//
// @Controller
// @RequestMapping("/dashboard/administrator")
// public class DashboardAdministratorController extends AbstractController {
//
// // Supporting services -----------------------------------------------------------
//
// @Autowired
// private ApplicationService applicationService;
//
// @Autowired
// private TripService tripService;
//
// @Autowired
// private OfferService offerService;
//
// @Autowired
// private RequestService requestService;
//
// @Autowired
// private MessageService messageService;
//
// @Autowired
// private CustomerService customerService;
//
// @Autowired
// private CommentService commentService;
//
//
// // Constructors -----------------------------------------------------------
//
// public DashboardAdministratorController() {
// super();
// }
//
// // Dashboard -----------------------------------------------------------
//
// @RequestMapping(value = "/dashboard", method = RequestMethod.GET)
// public ModelAndView dashboard() {
//
// ModelAndView result;
// Double bookAPL;
// Double bookDPL;
// Double bookAPT;
// Double bookDPT;
// Collection<Lessor> lessorMBA;
// Collection<Lessor> lessorMBD;
// Collection<Lessor> lessorMBP;
// Collection<Tenant> tenantMBA;
// Collection<Tenant> tenantMBD;
// Collection<Tenant> tenantMBP;
// Collection<Lessor> lessorMaxABB;
// Collection<Lessor> lessorMinABB;
// Collection<Tenant> tenantMaxABB;
// Collection<Tenant> tenantMinABB;
// Lessor maxL;
// Lessor minL;
// Tenant maxT;
// Tenant minT;
// Double maxRPF;
// Double minRPF;
// Double avgRPF;
// Double maxAPP;
// Double minAPP;
// Double avgAPP;
// Collection<Attribute> attributeSMTUP;
// Double maxSIPA;
// Double minSIPA;
// Double avgSIPA;
// Double maxIPT;
// Double minIPT;
// Double avgIPT;
// Double totalMoney;
// Double avgBPP1A;
// Double avgBPPNA;
//
// bookAPL = this.numbers(this.lessorService.avgAcceptedPerLessor());
// bookDPL = this.numbers(this.lessorService.avgDeniedPerLessor());
// bookAPT = this.numbers(this.tenantService.avgAcceptedPerTenant());
// bookDPT = this.numbers(this.tenantService.avgDeniedPerTenant());
// lessorMBA = this.lessors(this.lessorService.lessorNumApproved());
// lessorMBD = this.lessors(this.lessorService.lessorNumDenied());
// lessorMBP = this.lessors(this.lessorService.lessorNumPending());
// tenantMBA = this.tenants(this.tenantService.tenantsMoreRequestsApproved());
// tenantMBD = this.tenants(this.tenantService.tenantsMoreRequestsDenied());
// tenantMBP = this.tenants(this.tenantService.tenantsMoreRequestsPending());
// lessorMaxABB = new ArrayList<Lessor>();
// lessorMinABB = new ArrayList<Lessor>();
// maxL = this.lessorService.lessorMaxRatio();
// minL = this.lessorService.lessorMinRatio();
// if (maxL != null)
// lessorMaxABB.add(maxL);
// if (minL != null)
// lessorMinABB.add(minL);
// tenantMaxABB = new ArrayList<Tenant>();
// tenantMinABB = new ArrayList<Tenant>();
// maxT = this.tenantService.tenantMaxRatio();
// minT = this.tenantService.tenantMinRatio();
// if (maxT != null)
// tenantMaxABB.add(maxT);
// if (minT != null)
// tenantMinABB.add(minT);
// maxRPF = this.numbers(this.finderService.maxResultsPerFinder());
// minRPF = this.numbers(this.finderService.minResultsPerFinder());
// avgRPF = this.numbers(this.finderService.avgResultsPerFinder());
// maxAPP = this.numbers(this.auditService.findMaxAuditsOfProperties());
// minAPP = this.numbers(this.auditService.findMinAuditsOfProperties());
// avgAPP = this.numbers(this.auditService.findAvgAuditsOfProperties());
// attributeSMTUP = this.attributes(this.attributeService.findListAttributesSortedByTimesUsed());
// maxSIPA = this.numbers(this.socialIdentityService.findMaxSocialIdentities());
// minSIPA = this.numbers(this.socialIdentityService.findMinSocialIdentities());
// avgSIPA = this.numbers(this.socialIdentityService.findAvgSocialIdentities());
// maxIPT = this.numbers(this.invoiceService.findMaxInvoicesOfTenants());
// minIPT = this.numbers(this.invoiceService.findMinInvoicesOfTenants());
// avgIPT = this.numbers(this.invoiceService.findAvgInvoicesOfTenants());
// totalMoney = this.numbers(this.invoiceService.totalMoney());
// avgBPP1A = this.numbers(this.bookService.findAvgBooksProperty1Audit());
// avgBPPNA = this.numbers(this.bookService.findAvgBooksPropertyNoAudit());
//
// result = new ModelAndView("administrator/dashboard");
// result.addObject("bookAPL", bookAPL);
// result.addObject("bookDPL", bookDPL);
// result.addObject("bookAPT", bookAPT);
// result.addObject("bookDPT", bookDPT);
// result.addObject("lessorMBA", lessorMBA);
// result.addObject("lessorMBD", lessorMBD);
// result.addObject("lessorMBP", lessorMBP);
// result.addObject("tenantMBA", tenantMBA);
// result.addObject("tenantMBD", tenantMBD);
// result.addObject("tenantMBP", tenantMBP);
// result.addObject("lessorMaxABB", lessorMaxABB);
// result.addObject("lessorMinABB", lessorMinABB);
// result.addObject("tenantMaxABB", tenantMaxABB);
// result.addObject("tenantMinABB", tenantMinABB);
// result.addObject("maxRPF", maxRPF);
// result.addObject("minRPF", minRPF);
// result.addObject("avgRPF", avgRPF);
// result.addObject("maxAPP", maxAPP);
// result.addObject("minAPP", minAPP);
// result.addObject("avgAPP", avgAPP);
// result.addObject("attributeSMTUP", attributeSMTUP);
// result.addObject("maxSIPA", maxSIPA);
// result.addObject("minSIPA", minSIPA);
// result.addObject("avgSIPA", avgSIPA);
// result.addObject("maxIPT", maxIPT);
// result.addObject("minIPT", minIPT);
// result.addObject("avgIPT", avgIPT);
// result.addObject("totalMoney", totalMoney);
// result.addObject("avgBPP1A", avgBPP1A);
// result.addObject("avgBPPNA", avgBPPNA);
// result.addObject("requestURI", "dashboard/administrator/dashboard.do");
//
// return result;
//
// }
// //Ancillary methods -----------------------------------
//
// public Collection<Lessor> lessors(final Collection<Lessor> lessors) {
// Collection<Lessor> result;
//
// result = new ArrayList<Lessor>();
//
// if (lessors != null)
// result.addAll(lessors);
//
// return result;
// }
//
// public Collection<Tenant> tenants(final Collection<Tenant> tenants) {
// Collection<Tenant> result;
//
// result = new ArrayList<Tenant>();
//
// if (tenants != null)
// result.addAll(tenants);
//
// return result;
// }
//
// public Collection<Attribute> attributes(final Collection<Attribute> attributes) {
// Collection<Attributee> result;
//
// result = new ArrayList<Attribute>();
//
// if (attributes != null)
// result.addAll(attributes);
//
// return result;
// }
//
// public Double numbers(final Double number) {
// Double result;
//
// result = 0.0;
//
// if (number != null)
// result = number;
//
// return result;
// }
//
//}
