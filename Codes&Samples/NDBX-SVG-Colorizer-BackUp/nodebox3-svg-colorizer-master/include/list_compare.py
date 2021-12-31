def compare( in_value, in_list, ref_list):

    list_length = len(in_list)
    out_value = 0

    for idx ,n in enumerate(in_list):
        if in_value <= n:
            break
        else:
             out_value  = idx

    return ref_list[out_value]

'''
liste = [0,3,9,12]
color = ["A","B","C","D"]

index = compare(2, liste, color)
print index
'''
