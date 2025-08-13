from PIL import Image

def pixelate_image(image_path, output_path, pixel_size):
    # 加载图片并调整大小
    img = Image.open(image_path)
    resized_img = img.resize((img.width // pixel_size, img.height // pixel_size), Image.NEAREST)
    
    # 保存结果
    resized_img.save(output_path)

if __name__ == "__main__":
    pixelate_image(input("image_path: "), input("output_path: "), int(input("pixel_size: ")))
