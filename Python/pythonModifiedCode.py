import pandas as pd

# Sample DataFrame
data = {
    'sourcecode': ['ABC12', 'DFG'],
    'intermeidatecode': ['SCR', 'ab'],
    'targetcode': ['ABC12_2005f', '345-dfg']
}

df = pd.DataFrame(data)

# Function to create modifiedcode
def modify_code(row):
    sourcecode = row['sourcecode']
    intermediate = row['intermeidatecode']
    targetcode = row['targetcode']
    

    if '_' in targetcode:
        modified_part = targetcode.replace(sourcecode, '')
        modified_code = f"{intermediate}{modified_part}"
    elif '-' in targetcode:
        modified_part = targetcode.replace('-' + sourcecode.lower(), '')
        modified_code = f"{intermediate}{'-'}{modified_part}"


    return modified_code

# Apply the function to each row
df['modifiedcode'] = df.apply(modify_code, axis=1)

# Output the final DataFrame
print(df)
