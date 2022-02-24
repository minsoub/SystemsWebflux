@Controller
public class ImageController {
    private static final String BASE_PATH = "/images";
    private static final String FILENAME = "{filename:.*}";

    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @GetMapping(value = BASE_PATH + "/view/" + FILENAME, produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public Mono<ResponseEntity<?>> oneImage(@PathVariable String filename) {
        return imageService.getOneImage(filename)
                .map(resource -> {
                   try {
                       return ResponseEntity.ok()
                               .contentLength(resource.contentLength())
                               .body(new InputStreamResource(
                                       resource.getInpuStream()
                               ));
                   }catch(IOException e) {
                       return ResponseEntity.badRequest()
                               .body("Counldn't find " + filename + " => " + e.getMessage());
                   }
                });
    }

    @PostMapping(value = BASE_PATH)
    public Mono<String> createFile(@RequestPart(name = "file") Flux<FilePart> files) {
        return imageService.createImage(files)
                .then(Mono.just("redirect:/"));
    }

    @DeleteMapping(BASE_PATH + "/delete/" + filename)
    public Mono<String> deleteFile(@PathVariable String filename) {
        return imageService.deleteImage(filename)
                .then(Mono.just("redirect:/"));
    }

    @GetMapping("/")
    public Mono<String> index(Model model) {
        model.addAttribute("images", imageService.allImages());
        return Mono.just("index");
    }
}